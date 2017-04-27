package ncr.res.mobilepos.searchapi.resource;

import javax.servlet.ServletContext;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import com.wordnik.swagger.annotations.Api;
import ncr.realgate.util.Trace;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;

/**
 * SearchApiResource class is a web resourse which provides support for search
 * customer.
 */
@Path("/searchapi")
@Api(value="/SearchApi", description="åüçıAPI")
public class SearchApiResource {

	/** A private member variable used for the servlet context. */
	@Context
	private ServletContext context;

	/** The instance of the trace debug printer. */
	private Trace.Printer tp = null;

	/** A private member variable used for logging the class implementations. */
	private static final Logger LOGGER = (Logger) Logger.getInstance();

	/** */
	private static final String PROG_NAME = "SearchApiResource";

	/**
	 * constructor.
	 */
	public SearchApiResource() {
		tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
				getClass());
	}

}
