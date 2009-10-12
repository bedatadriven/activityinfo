package org.activityinfo.server.report;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.activityinfo.server.DbUnitTestCase;
import org.activityinfo.server.report.renderer.html.HtmlChartRenderer;
import org.activityinfo.server.report.renderer.html.HtmlChartRendererJC;
import org.activityinfo.server.report.renderer.html.ImageStorageProvider;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public abstract class ReportDbUnitTest extends DbUnitTestCase {

	protected class TestModule extends AbstractModule {

		@Override
		protected void configure() {
			bind(HtmlChartRenderer.class).to(HtmlChartRendererJC.class);
			bind(ImageStorageProvider.class).to(NullImageStorageProvider.class);
		}
		
		@Provides @Singleton EntityManagerFactory provideEmf() {
			return ReportDbUnitTest.emf;
		}
		
		@Provides @Singleton EntityManager provideEm(EntityManagerFactory emf) {
			return emf.createEntityManager();
		}
	}
	
	
}
