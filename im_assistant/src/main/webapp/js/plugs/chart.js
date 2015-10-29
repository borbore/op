
//************************************************************
// Data notice the structure
//************************************************************
drawVisitorLineChart('canvasWeek',2000,[1206,1962,3000,5506,4000,5214,8015,6000,3000,2452,2635,1257,9000,522,655,465,365],['4月25日','4月26日','4月27日','4月28日','4月29日','4月30日','5月01日','5月02日','5月03日','5月04日','5月05日','5月06日','5月07日','5月08日','5月09日','5月10日','5月11日']);

function drawVisitorLineChart(canvasID,scale,dataList,horizonalLabel)
{
	//寻找数组最大值，确定纵轴高度
	var maxValue=10000;
	var height=Math.floor(maxValue/scale)*40;
	//确定横向宽度
	var width=horizonalLabel.length*80;
	var canvas=document.getElementById(canvasID);
	var context=canvas.getContext("2d");

	context.beginPath();
	context.moveTo(30,20);
	context.lineTo(30,height);
	context.closePath();
	context.strokeStyle='rgb(224,224,224)';
	context.lineWidth=1;
	context.stroke();

	//添加网格线与坐标轴
	context.beginPath();
	context.moveTo(30,height);
	context.lineTo(width+60,height);
	context.closePath();
	context.strokeStyle='rgb(224,224,224)';
	context.lineWidth=1;
	context.stroke();
	context.strokeStyle='rgb(237,237,237)';
	context.lineWidth=1;

	for(var i=0;i<Math.floor(maxValue/scale);i++)
	{
		context.font="200 12px 微软雅黑";
		context.fillStyle='rgb(167,167,167)';
		context.fillText((i+1)*scale/1000+'k',5,height-(i+1)*35+5);
		//horizontalDashlineTo(context,30,height-(i+1)*50,60+width,height-(i+1)*50);
	}
	context.fillText("0",5,height);

	for(var i=0;i<horizonalLabel.length+1;i++)
	{
		context.beginPath();
		context.moveTo(45+i*70,height);
		context.lineTo(45+i*70,height+8);
		context.strokeStyle='rgb(237,237,237)';
		context.lineWidth=1;
		context.closePath();
		context.stroke();
		//verticalDashlineTo(context,45+i*80,0,45+i*80,height);
	}
	context.font="100 12px 微软雅黑";
	context.fillStyle='rgb(153,153,153)';
	for(var i=0;i<horizonalLabel.length;i++)
	{
		context.fillText(horizonalLabel[i],60+i*70,height+30);
	}
	//绘制Line Chart
	context.beginPath();
	context.moveTo(80,height-((dataList[0]/scale)*35));
	context.strokeStyle='rgb(126,224,216)';
	context.lineWidth=2;
	context.stroke();
	for(var i=0;i<horizonalLabel.length;i++)
	{
		context.strokeStyle='rgb(126,224,216)';
		context.lineWidth=1;
		context.lineTo(80+i*70,height-((dataList[i]/scale)*35));
		context.stroke();
	}
	//绘制数据点
	for(var i=0;i<horizonalLabel.length;i++)
	{
		context.beginPath();
		context.arc(80+i*70,height-((dataList[i]/scale)*35),4,0,Math.PI*2,false);
		context.closePath();
		context.fillStyle="rgba(173,190,210,1)";
		context.strokeStyle='rgb(173,190,210)';
		context.lineWidth=3;
		context.fill();
		context.stroke();
		context.font="200 12px 微软雅黑";
		context.fillStyle='rgb(0,0,0)';
		context.fillText(dataList[i],70+i*70,height-20-((dataList[i]/scale)*35));

	}
}
/*
//绘制横向虚线
function horizontalDashlineTo(context,x1,y1,x2,y2)
{
	context.beginPath();
	var horizontal=Math.floor((x2-x1)/8);
	for(var i=0;i<horizontal;i++)
	{
		context.moveTo(x1+i*8,y1);
		context.lineTo(x1+i*8+4,y2);
	}
	context.moveTo(x1+horizontal*8,y1);
	context.lineTo(x2,y2);
	context.closePath();
	context.stroke();
}
//绘制纵向虚线
function verticalDashlineTo(context,x1,y1,x2,y2)
{
	context.beginPath();
	var vertical=Math.floor((y2-y1)/8);
	for(var i=0;i<vertical;i++)
	{
		context.moveTo(x1,y1+i*8);
		context.lineTo(x2,y1+i*8+4);
	}
	context.moveTo(x1,y1+vertical*8);
	context.lineTo(x2,y2);
	context.closePath();
	context.stroke();
}*/
