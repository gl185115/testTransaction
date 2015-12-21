package ncr.res.mobilepos.exception.provider;

import java.util.logging.Logger;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;

/**
 * An exception mapper class for unhandled exception thrown by the server,
 * that response OK(200) with error codes in an entity object.
 */
@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {
	private static final Logger LOGGER = Logger
			.getLogger(GenericExceptionMapper.class.getName());

	@Override
	public Response toResponse(Throwable throwable) {
		ResultBase result = new ResultBase(ResultBase.RES_SERVER_ERROR_5XX);

		if (throwable instanceof UnsatisfiedLinkError) {
			result.setNCRWSSExtendedResultCode(ResultBase.RES_SERVER_LIBRARYNOTFOUND);
		} else if (throwable instanceof WebApplicationException) {
			result.setNCRWSSResultCode(ResultBase.RES_CLIENT_ERROR_4XX);
			result.setNCRWSSExtendedResultCode(((WebApplicationException) throwable)
					.getResponse().getStatus());
		}
		
		String errorMessage = StringUtility.printStackTrace(throwable);
		result.setMessage(errorMessage);
		LOGGER.severe(errorMessage);

		return Response.status(Response.Status.OK).entity(result).build();
	}

}
