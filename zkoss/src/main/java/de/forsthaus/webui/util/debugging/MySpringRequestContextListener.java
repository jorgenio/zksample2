package de.forsthaus.webui.util.debugging;

import javax.servlet.ServletRequestEvent;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.request.ServletRequestAttributes;

public class MySpringRequestContextListener extends RequestContextListener {

	private static final String REQUEST_ATTRIBUTES_ATTRIBUTE = RequestContextListener.class.getName() + ".REQUEST_ATTRIBUTES";

	@Override
	public void requestInitialized(ServletRequestEvent requestEvent) {
		if (!(requestEvent.getServletRequest() instanceof HttpServletRequest)) {
			throw new IllegalArgumentException("Request is not an HttpServletRequest: " + requestEvent.getServletRequest());
		}
		HttpServletRequest request = (HttpServletRequest) requestEvent.getServletRequest();
		ServletRequestAttributes attributes = new ServletRequestAttributes(request);
		request.setAttribute(REQUEST_ATTRIBUTES_ATTRIBUTE, attributes);
		LocaleContextHolder.setLocale(request.getLocale());
		RequestContextHolder.setRequestAttributes(attributes);

		System.out.println(request.getSession().toString());
	}

	@Override
	public void requestDestroyed(ServletRequestEvent requestEvent) {
		ServletRequestAttributes attributes = (ServletRequestAttributes) requestEvent.getServletRequest().getAttribute(REQUEST_ATTRIBUTES_ATTRIBUTE);
		ServletRequestAttributes threadAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (threadAttributes != null) {
			// We're assumably within the original request thread...
			if (attributes == null) {
				attributes = threadAttributes;
			}
			LocaleContextHolder.resetLocaleContext();
			RequestContextHolder.resetRequestAttributes();
		}
		if (attributes != null) {
			attributes.requestCompleted();
		}
	}
}
