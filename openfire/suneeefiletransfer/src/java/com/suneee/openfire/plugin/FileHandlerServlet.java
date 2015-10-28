package com.suneee.openfire.plugin;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.jivesoftware.admin.AuthCheckFilter;
import org.jivesoftware.util.JiveGlobals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides support for downloading the Jive Spark IM client. (<a
 * href="http://www.igniterealtime.org/projects/spark/index.jsp">Spark</a>).
 * <p>
 * <p/>
 * 
 * @author Derek DeMoro
 */
public class FileHandlerServlet extends HttpServlet {

	private static final Logger logger = LoggerFactory
			.getLogger(FileHandlerServlet.class);

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		AuthCheckFilter.addExclude("suneeefiletransfer/upload/*");
		AuthCheckFilter.addExclude("suneeefiletransfer/download/*");
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String uri = request.getRequestURI();
		if (uri.contains("suneeefiletransfer/upload")) {
			request.setCharacterEncoding(JiveGlobals.getProperty(
					"com.suneee.filetransfer.character.encoding", "UTF-8"));
			response.setCharacterEncoding(JiveGlobals.getProperty(
					"com.suneee.filetransfer.character.encoding", "UTF-8"));
			response.setContentType("text/json; charset=" + "UTF-8");
			File suneeeFileUploadDir = new File(JiveGlobals.getHomeDirectory(),
					"suneeefileupload");
			if (!suneeeFileUploadDir.exists()) {
				suneeeFileUploadDir.mkdirs();
			}
			
			DiskFileUpload upload = new DiskFileUpload();
			List items = null;
			try {
				items = upload.parseRequest(request);
			} catch (Exception e) {
				// I'm going to ignore this Greg. That's right.
			}

			List<String> result = new ArrayList<String>();
			logger.debug("items:"+items);
			System.out.println("items:"+items);
			if (items != null) {
				String key = null; // 原图key
				String key2 = null; // 缩略图key2
				boolean zipImage = false; // 是否生成缩略图
				for (Object item : items) {
					FileItem fileItem = (FileItem) item;
					if (fileItem.isFormField()) {
						String fieldName = fileItem.getFieldName();
						if ("key".equals(fieldName)) {
							key = fileItem.getString();
						} else if ("key2".equals(fieldName)) {
							key2 = fileItem.getString();
						} else if ("zipImage".equals(fieldName)) {
							zipImage = "true".equals(fileItem.getString());
						}
					}
				}
				System.out.println("key==：" + key);
				System.out.println("key2==：" + key2);
				System.out.println("zipImage==：" + zipImage);
				for (Object item : items) {
					FileItem fileItem = (FileItem) item;
					if (!fileItem.isFormField()) {
						String fieldName = fileItem.getFieldName();
						System.out.println("fieldName==：" + fieldName);
						if (true || "theFile".equals(fieldName)) { // 不判断字段名
							String fileName = fileItem.getName();
							System.out.println("fileName==：" + fileName);
							byte[] data = fileItem.get();
							String ext = "";
							if (fileName != null
									&& fileName.trim().length() > 0) {
								if (key == null || key.trim().length() == 0) {
									key = this.randomID();
									if (fileName.lastIndexOf(".") != -1) {
										ext = fileName.substring(fileName
												.lastIndexOf("."));
									}
									key += ext;

								}
								String basePath = new StringBuffer(
										key.substring(0, 4)).append("/")
										.append(key.substring(4, 6))
										.append("/")
										.append(key.substring(6, 8)).toString();
								File baseFile = new File(suneeeFileUploadDir,
										basePath);
								if (!baseFile.exists()) {
									baseFile.mkdirs();
								}
								FileOutputStream faos = null;
								File keyFile = new File(baseFile, key);
								try {
									if (!keyFile.exists()) {
										keyFile.createNewFile();
									}
									faos = new FileOutputStream(keyFile);
									faos.write(data);
									faos.flush();
								} catch (Exception e) {
									if (keyFile.exists()) {
										keyFile.delete();
									}
									response.getWriter().write(
											"{\"status\": \"error\", \"errorMsg\": \""
													+ e.getMessage() + "\"}");
									break;
								} finally {
									if (faos != null) {
										faos.close();
									}
								}
								result.add("{\"" + key + "\":" + data.length
										+ "}");

								String ContentType = fileItem.getContentType()
										.toLowerCase();
								System.out.println("ContentType==："
										+ keyFile.getAbsolutePath());
								if (keyFile.exists() && zipImage// &&
																// ContentType.contains("image")
								) { // 生成缩略图
									System.out.println("开始生成缩略图："
											+ keyFile.getAbsolutePath());
									if (key2 == null
											|| key2.trim().length() == 0) {
										key2 = this.randomID();
										key2 += ext;
									}

									String zipImageWidth = JiveGlobals
											.getProperty("com.suneee.filetransfer.zipImageWidth");
									String zipImageHeight = JiveGlobals
											.getProperty("com.suneee.filetransfer.zipImageHeight");
									int zipImageW = 0;
									int zipImageH = 0;
									if (zipImageWidth != null
											&& zipImageWidth.trim().length() > 0) {
										zipImageW = Integer
												.parseInt(zipImageWidth);
									}
									if (zipImageHeight != null
											&& zipImageHeight.trim().length() > 0) {
										zipImageH = Integer
												.parseInt(zipImageHeight);
									}
									File zipFile = this.zipImage(keyFile,
											new File(baseFile, key2),
											zipImageW, zipImageH);
									if (zipFile.exists()) {
										result.add("{\"" + key2 + "\":"
												+ zipFile.length() + "}");
										System.out.println("生成缩略图完成："
												+ zipFile.getAbsolutePath());
									} else {
										System.out.println("生成缩略图出错："
												+ zipFile.getAbsolutePath());
									}
								}
							}

						}
					}
				}
			}
			StringBuilder json = new StringBuilder();
			if (result.size() > 0) {
				json.append("{\"status\":\"success\",\"files\":[");
				for (String fileName : result) {
					json.append(fileName).append(",");
				}
				if (result.size() > 0) {
					json.deleteCharAt(json.length() - 1);
				}
				json.append("]}");
				response.getWriter().write(json.toString()); // 返回保存路径及文件大小
			}else{
				json.append("{\"status\":\"error\",\"errorMsg\": \" ");
				json.append("Didn't get to the file!");
				json.append(" \"}");
				response.getWriter().write(json.toString()); // 返回保存路径及文件大小
			}
			System.out.println("json:"+json.toString());
			logger.debug("json:"+json.toString());
		} else if (uri.contains("suneeefiletransfer/download")) {
			String key = uri.substring(uri
					.indexOf("suneeefiletransfer/download/")
					+ "suneeefiletransfer/download/".length());
			String basePath = new StringBuffer(key.substring(0, 4)).append("/")
					.append(key.substring(4, 6)).append("/")
					.append(key.substring(6, 8)).toString();
			File suneeeFileUploadDir = new File(JiveGlobals.getHomeDirectory(),
					"suneeefileupload");
			File baseFile = new File(suneeeFileUploadDir, basePath);
			File file = new File(baseFile, key);
			if (suneeeFileUploadDir.exists() && baseFile.exists()
					&& file.exists()) {
				FileNameMap fileNameMap = URLConnection.getFileNameMap();
				String type = fileNameMap.getContentTypeFor(file.getName());
				if (null!=type && type.contains("image")) {
					response.setContentType(type);
				} else {
					response.setContentType("application/octet-stream;charset=UTF-8");
					response.setHeader("Content-Disposition",
							"attachment; filename=" + file.getName());
					response.setHeader("Content-Length",
							String.valueOf(file.length()));
				}

				// Open the file and output streams
				FileInputStream in = new FileInputStream(file);
				OutputStream out = response.getOutputStream();

				// Copy the contents of the file to the output stream
				byte[] buf = new byte[1024];
				int count;
				while ((count = in.read(buf)) >= 0) {
					out.write(buf, 0, count);
				}
				in.close();
				out.close();
			} else {
				response.getWriter().write("文件不存在：" + file.getName());
			}
		}

	}

	/**
	 * 生成一个随机id。
	 * 
	 * <pre>
	 * 
	 * Description：
	 * 
	 *     详细说明。
	 * 
	 * </pre>
	 * 
	 * @return 随机id
	 */
	private String randomID() {

		Long now = Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmss")
				.format(new Date()));
		Long random = (long) (Math.random() * now);
		return new StringBuffer(String.valueOf(now)).append(random)
				.append(Thread.currentThread().getId()).toString();
	}

	/**
	 * 生成指定宽*高的缩略图。
	 * 
	 * <pre>
	 * 
	 * Description：
	 * 
	 *     详细说明。
	 * 
	 * </pre>
	 * 
	 * @param srcImagePath
	 *            要更新的图片全路径名
	 * @param descImagePath
	 *            改造后的图片全路径名
	 * @param dstWidth
	 *            缩略图片宽度
	 * @param dstHeight
	 *            缩略图片高度
	 * @return true 修改成功 false 修改失败
	 * @throws 异常类型
	 *             说明
	 */
	private File zipImage(File srcFile, File zipFile, int dstWidth,
			int dstHeight) {

		BufferedImage bufferedImage = null;
		Image srcImage = null;
		if (!srcFile.exists()) {
			logger.error("源图片在对应的路径下找不到。");
		} else {
			FileOutputStream fos = null;
			try {
				srcImage = ImageIO.read(srcFile);
				int srcWidth = srcImage.getWidth(null);// 原图片宽度
				int srcHeight = srcImage.getHeight(null);// 原图片高度
				if (dstWidth == 0 && dstHeight == 0) {
					dstWidth = 120;
					dstHeight = 120;
				} else if (dstWidth == 0) {
					float scale = (float) srcHeight / (float) dstHeight;
					dstWidth = Math.round((float) srcWidth / scale);
				} else if (dstHeight == 0) {
					float scale = (float) srcWidth / (float) dstWidth;
					dstHeight = Math.round((float) srcHeight / scale);
				}

				bufferedImage = new BufferedImage(dstWidth, dstHeight,
						BufferedImage.TYPE_INT_RGB);
				bufferedImage.getGraphics().drawImage(srcImage, 0, 0, dstWidth,
						dstHeight, null);
				// fos = new FileOutputStream(zipFile);
				// JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(fos);
				// encoder.encode(bufferedImage); // 近JPEG编码

				// String formatName =
				// zipFile.getName().substring(zipFile.getName().lastIndexOf(".")
				// + 1);
				// ImageIO.write(bufferedImage, formatName , zipFile);

			} catch (IOException e) {
				if (zipFile.exists()) {
					zipFile.delete();
				}
				logger.error(e.getMessage());
			} finally {
				bufferedImage = null;
				srcImage = null;
				try {
					if (fos != null) {
						fos.close();
					}
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
			}
		}
		return zipFile;
	}
}
