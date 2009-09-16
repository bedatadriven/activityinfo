package org.activityinfo.server.report;

public class FullTest {
//
//
//	public static class TestImageStorageProvider implements ImageStorageProvider {
//
//
//		public TestImageStorageProvider() {
//			(new File("target/report/img")).mkdirs();
//		}
//
//		@Override
//		public ImageStorage getImageUrl(String suffix)
//				throws IOException {
//			String fn = (new Date()).getTime() + suffix;
//
//			return new ImageStorage("img/" + fn, new FileOutputStream("target/report/img/" + fn));
//		}
//
//	}
//
//	public static class TestModule extends AbstractModule {
//
//		@Override
//		protected void configure() {
//
//			bind(ChartHtmlRenderer.class).to(ChartHtmlRendererJC.class);
//			bind(ChartPdfGenerator.class).to(ChartPdfRendererJC.class);
//
//			bind(ImageStorageProvider.class).to(TestImageStorageProvider.class);
//
//		}
//
//		@Provides @Singleton EntityManagerFactory provideEmf() {
//
//			return Persistence.createEntityManagerFactory("activityInfo");
//
//		}
//
//		@Provides @Singleton EntityManager provideEm(EntityManagerFactory emf) {
//			return emf.createEntityManager();
//		}
//	}
//
//	protected Report report;
//	protected Injector injector;
//	protected Map<String, Object> parameters;
//
//	@Test
//	public void test() throws IOException, SAXException, DocumentException {
//
//		/*
//		 *
//		 * Parse the XML report definition
//		 */
//
//		ReportParser parser = new ReportParser();
//		InputStream in = FullTest.class.getResourceAsStream("/report-def/full-test.xml");
//
//		parser.parse( new InputSource( in ));
//
//		report = parser.getReport();
//
//		/*
//		 *
//		 * Create parameters map
//		 */
//
//		parameters = new HashMap<String,Object>();
//		parameters.put("MONTH", new Date());
//
//		/*
//		 *
//		 * Generate
//		 */
//
//		injector = Guice.createInjector(new TestModule());
//
//
//		// generate html
//		writeHtml();
//		writeExcel();
//		writePdf();
//	}
//
//	protected void writeHtml() throws IOException {
//
//		ReportHtmlRenderer gtor = injector.getInstance(ReportHtmlRenderer.class);
//		gtor.setParameters(parameters);
//
//		HtmlWriter html = new HtmlWriter();
//		html.formatted = true;
//
//		html.startDocument();
//		html.startDocumentHeader();
//		html.documentTitle("Test Report");
//		html.charsetDeclaration("UTF-8");
//		html.styleSheetLink("report.css");
//		html.endDocumentHeader();
//
//		html.startDocumentBody();
//
//		gtor.render(html, report);
//
//		html.endDocumentBody();
//		html.endDocument();
//
//		// write to temp file
//
//		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
//				new FileOutputStream("target/report/full-test.html"),"UTF8"));
//		out.write(html.toString());
//		out.close();
//	}
//
//
//	protected void writeExcel() throws IOException {
//
//		ReportExcelRenderer gtor = injector.getInstance(ReportExcelRenderer.class);
//		gtor.setParameters(parameters);
//
//		HSSFWorkbook book = new HSSFWorkbook();
//
//		gtor.render(book, report);
//
//		OutputStream out =
//				new FileOutputStream("target/report/full-test.xls");
//		book.write(out);
//		out.close();
//	}
//
//	protected void writePdf() throws IOException, DocumentException {
//
//		Document document = new Document();
//		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("target/report/full-test.pdf"));
//
//		document.open();
//
//		AbstractDocGenerator gtor = injector.getInstance(AbstractDocGenerator.class);
//		gtor.setParameters(parameters);
//
//		gtor.addContent(writer, document, report);
//
//		document.close();
//
//	}
//
}
