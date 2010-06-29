package org.sigmah.server.report.util;

import java.util.Stack;

public class HtmlWriter {

	private StringBuilder sb = new StringBuilder();
	
	private HtmlTag pendingTag = null;
	private Stack<String> openTags = new Stack<String>();
	
	public boolean formatted = false;
	
	protected void writeTag(HtmlTag tag, boolean closed) {
		if(tag != null) {
			if(formatted) {
				sb.append("\r\n");
			}
			
			sb.append('<');
			sb.append(tag.getName());
			
			for(HtmlAttribute attrib : tag.getAttributes()) {
				sb.append(' ');
				sb.append(attrib.getName());
				sb.append("=\"");
				sb.append(attrib.getValue());
				sb.append("\"");
			}
			if(closed) { 
				sb.append("/>");
			} else {
				sb.append(">");
			}
		}	
	}
	
	protected void writePendingTag() {
		if(pendingTag!=null) {
			writeTag(pendingTag, (pendingTag.getInnerText()==null && pendingTag.isClosed()));
			
			if(pendingTag.getInnerText() != null) {
				sb.append(pendingTag.getInnerText());
	
				if(pendingTag.isClosed()) {
					sb.append("</");
					sb.append(pendingTag.getName());
					sb.append(">");
				}
			}
			
			if(!pendingTag.isClosed()) {
				openTags.push(pendingTag.getName());
			}
						
			pendingTag = null;
		}
	}
	
	public HtmlTag open(HtmlTag tag) {
		
		writePendingTag();
		
		pendingTag = tag;
		
		
		return tag;	
	}
	
	public HtmlWriter single(HtmlTag tag) {
		writePendingTag();
		writeTag(tag, true);		
		return this;
	}
	
	public HtmlWriter text(String text) {
		writePendingTag();
		sb.append(text);
		return this;
	}
	
	public HtmlWriter close() {
		
		writePendingTag();
		
		if(formatted) {
			sb.append("\r\n");
		}
		
		String tagName = openTags.pop();
		sb.append("</");
		sb.append(tagName);
		sb.append(">");
		
		return this;
	}
	
	public HtmlWriter closeVerify(String tagName) {
		
		
//		if(!openTags.peek().equals(tagName)) {
//			throw new MismatchedTagError(openTags.peek());
//		}
		return close();
	}
	
	public HtmlTag startDocument() {
		return open(new HtmlTag("html"));
	}
	
	public HtmlTag startDocumentHeader() {
		return open(new HtmlTag("head"));
	}
	
	public HtmlTag startDocumentBody() {
		return open(new HtmlTag("body"));
	}

	public HtmlTag documentTitle(String text) {
		return open((new HtmlTag("title")).text(text).close());
	}
	
	public HtmlTag httpHeader(String header, String value) {
		return open(new HtmlTag("meta"))
				.at("http-equiv", header)
				.at("content", value)
				.close();
	}
	
	public HtmlTag charsetDeclaration(String charset) {
		return httpHeader("Content-Type", "text/html; charset=" + charset);
	}
	
	public HtmlTag styleSheetLink(String cssPath) {
		return open(new HtmlTag("link"))
				.at("rel", "stylesheet")
				.at("type", "text/css")
				.at("href", cssPath)
				.close();
	}

	public HtmlTag startDiv() {
		return open(new HtmlTag("div"));
	}
	
	public HtmlTag div(String text) {
		return startDiv().text(text).close();
	}
	
	public HtmlTag div() {
		return startDiv().close();
	}
	
	public HtmlTag image(String url) {
		return open(new HtmlTag("img")).at("src", url).close();
	}
	
	public HtmlTag startTable() {
		return open(new HtmlTag("table"));
	}
	
	
	public HtmlTag startTableRow() {
		return open(new HtmlTag("tr"));
	}
	
	public HtmlTableCellTag startTableCell() {
		return (HtmlTableCellTag) open(new HtmlTableCellTag());
	}
	
	public HtmlTableCellTag tableCell(String innerText) {
		return (HtmlTableCellTag) open((new HtmlTableCellTag())
				.text(innerText)
				.close());
	}
     public HtmlWriter newLine() {
        return text("<br>");
    }
	
	public HtmlTableCellTag emptyTableCell() {
		return (HtmlTableCellTag) open((new HtmlTableCellTag())
				.nbsp()
				.close());
	}
	
	public HtmlTag startHeader(int level) {
		return open(new HtmlTag("h" + level));
	}
	
	public HtmlTag header(int level, String text) {
		return startHeader(level).text(text).close();
	}

    public HtmlWriter paragraph(String text) {
        open(new HtmlTag("p")).text(text).close();
        return this;
    }

	public HtmlTag header(int level) {
		return startHeader(level).close();
	}
	
	public HtmlTag startTableHeader() {
		return open(new HtmlTag("thead"));
	}
	
	public HtmlTag startTableBody() {
		return open(new HtmlTag("tbody"));
	}
	
	public HtmlWriter endDiv() {
		return closeVerify("div");
	}
	
	public HtmlWriter endTable() {
		return closeVerify("table");
	}
	
	public HtmlWriter endTableRow() {
		return closeVerify("tr");
	}
	public HtmlWriter endTableCell() {
		return closeVerify("td");
	}
	
	public HtmlWriter endTableHeader() {
		return closeVerify("thead");
	}
	
	public HtmlWriter endTableBody() {
		return closeVerify("tbody");
	}
	
	public HtmlWriter endHeader(int level) {
		return closeVerify("h" + level);
	}
	
	
	public HtmlWriter endDocument() { 
		return closeVerify("html");
	}
	
	public HtmlWriter endDocumentBody() {
		return closeVerify("body");
	}
	
	public HtmlWriter endDocumentHeader() {
		return closeVerify("head");
	}

	@Override
	public String toString() {
		return sb.toString();
	}


}
