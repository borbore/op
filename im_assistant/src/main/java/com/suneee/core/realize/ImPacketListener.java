package com.suneee.core.realize;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster.SubscriptionMode;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.RosterPacket;

import com.suneee.core.util.AssertUtil;
import com.suneee.core.util.ImGlobals;
/**
 * @author xiepeng
 *
 */
public class ImPacketListener implements PacketListener {

	private Connection connection;

	public ImPacketListener(Connection conn) {
		this.connection = conn;
	}

	public void processPacket(Packet packet) {
		Presence presence = (Presence) packet;
		String from = presence.getFrom();
		String subscriptionMode = ImGlobals.getSubscriptionMode();
		RosterEntry rosterEntry = connection.getRoster().getEntry(from);
		if (rosterEntry != null
				&& rosterEntry.getType() == RosterPacket.ItemType.to) {
			if (presence.getType() == Presence.Type.subscribe) {
				Presence response = new Presence(Presence.Type.subscribed);
				response.setTo(from);
				connection.sendPacket(response);
			}
		} else {
			if (presence.getType() == Presence.Type.subscribe) {
				if (AssertUtil.isNotFullOfRoster(connection, presence.getTo())) {
					// 接爱好友请求，并将对方加为好友
					if (subscriptionMode.equals(SubscriptionMode.accept_all.toString())) {
						Presence response = new Presence(Presence.Type.subscribed);
						response.setTo(from);
						connection.sendPacket(response);
						// 将对方加为好友
						Presence reqeuest = new Presence(Presence.Type.subscribe);
						reqeuest.setTo(from);
						connection.sendPacket(reqeuest);
						// 拒绝好友请求
					} else if (subscriptionMode.equals(SubscriptionMode.reject_all.toString())) {
						Presence response = new Presence(Presence.Type.unsubscribed);
						response.setTo(from);
						connection.sendPacket(response);
					} else {
						// 接受好友请求，等待我方确认
						/*Presence response = new Presence(Presence.Type.subscribed);
						response.setTo(from);
						connection.sendPacket(response);*/
						//有发好友请求，给管理员发送信息
						ImGlobals.sendMessageToAdmin(connection, "add friend", "from : " + from + "  to : " + presence.getTo());
						
					}
				} else {
					Presence response = new Presence(Presence.Type.unsubscribed);
					response.setTo(from);
					connection.sendPacket(response);
				}
			} else if (presence.getType() == Presence.Type.unsubscribe) {
				Presence response = new Presence(Presence.Type.unsubscribed);
				response.setTo(from);
				connection.sendPacket(response);
			}
		}
	}
}
