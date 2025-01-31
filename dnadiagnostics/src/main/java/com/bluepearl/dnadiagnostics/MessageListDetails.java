package com.bluepearl.dnadiagnostics;

public class MessageListDetails
{
	private String fullmessage;
	private String MessageState;
	private String CreatedDate;
	private String MessageFrom;
	private String MessageTo;

	private String ReplyMessage;
	private String ReplyBy;
	private String ModifiedDate;
	private String MesssageTansactionId;
	private String CloseBy;


	public MessageListDetails(
			String fullmessage,
			String MessageState,
			String CreatedDate,
			String MessageFrom,
			String MessageTo,
			String ReplyMessage,
			String ReplyBy,
			String ModifiedDate,
			String MesssageTansactionId,
			String CloseBy

	)
	{
		this.fullmessage = fullmessage;
		this.MessageState = MessageState;
		this.CreatedDate = CreatedDate;
		this.MessageFrom = MessageFrom;
		this.MessageTo = MessageTo;

		this.ReplyMessage = ReplyMessage;
		this.ReplyBy = ReplyBy;
		this.ModifiedDate = ModifiedDate;
		this.MesssageTansactionId = MesssageTansactionId;
		this.CloseBy = CloseBy;

	}


	public MessageListDetails()
	{
		// TODO Auto-generated constructor stub
	}

	public void setFullMessage(String fullmessage)
	{
		this.fullmessage = fullmessage;
	}
	public void setMessageState(String MessageState) {
		this.MessageState = MessageState;
	}
	public void setCreatedDate(String CreatedDate) {
		this.CreatedDate = CreatedDate;
	}

	public void setMessageFrom(String MessageFrom) {
		this.MessageFrom = MessageFrom;
	}
	public void setMessageTo(String MessageTo) {
		this.MessageTo = MessageTo;
	}

	public void setReplyMessage(String ReplyMessage) {
		this.ReplyMessage = ReplyMessage;
	}

	public void setReplyBy(String ReplyBy) {
		this.ReplyBy = ReplyBy;
	}
	public void setModifiedDate(String ModifiedDate) {
		this.ModifiedDate = ModifiedDate;
	}
	public void setMesssageTansactionId(String MesssageTansactionId) {
		this.MesssageTansactionId = MesssageTansactionId;
	}
	public void setmsg_CloseBy(String CloseBy) {
		this.CloseBy = CloseBy;
	}


	public String getfullmessage()
	{
		return this.fullmessage;
	}
	public String getMessageState()
	{
		return this.MessageState;
	}
	public String getCreatedDate()
	{
		return this.CreatedDate;
	}

	public String getMessageFrom()
	{
		return this.MessageFrom;
	}
	public String getMessageTo()
	{
		return this.MessageTo;
	}
	public String getModifiedDate()
	{
		return this.ModifiedDate;
	}
	public String getReplyBy()
	{
		return this.ReplyBy;
	}
	public String getReplyMessage()
	{
		return this.ReplyMessage;
	}

	public String getMesssageTansactionId()
	{
		return this.MesssageTansactionId;
	}
	public String getCloseBy()
	{
		return this.CloseBy;
	}


}
