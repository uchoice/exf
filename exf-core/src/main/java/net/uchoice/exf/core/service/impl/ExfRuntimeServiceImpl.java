package net.uchoice.exf.core.service.impl;

import net.uchoice.exf.client.exception.ErrorMessage;
import net.uchoice.exf.client.exception.ExfException;
import net.uchoice.exf.client.exception.ExfRuntimeException;
import net.uchoice.exf.client.service.ExfRequest;
import net.uchoice.exf.client.service.ExfResponse;
import net.uchoice.exf.client.service.ExfRuntimeService;
import net.uchoice.exf.core.config.Service;
import net.uchoice.exf.core.config.ServiceManager;
import net.uchoice.exf.core.identify.Identify;
import net.uchoice.exf.core.matcher.exception.MatchException;
import net.uchoice.exf.core.runtime.Session;
import net.uchoice.exf.core.trace.ExfTracker;
import net.uchoice.exf.core.trace.Record;
import net.uchoice.exf.core.trace.RecordType;
import net.uchoice.exf.model.action.ActionResult;
import net.uchoice.exf.model.action.ActionState;
import net.uchoice.exf.model.container.ContainerInst;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExfRuntimeServiceImpl implements ExfRuntimeService {

	private static final Logger LOG = LoggerFactory.getLogger(ExfRuntimeServiceImpl.class);

	@Override
	public ExfResponse request(ExfRequest request) {
		ExfResponse response = new ExfResponse();
		Session session = null;
		try {
			if (null == request) {
				throw new ExfException(ErrorMessage.code(true, "C-EXF-01-01-001"));
			}
			if (StringUtils.isBlank(request.getServiceCode())) {
				throw new ExfException(ErrorMessage.code(true, "C-EXF-03-01-001"));
			}
			if (StringUtils.isBlank(request.getVersion())) {
				throw new ExfException(ErrorMessage.code(true, "C-EXF-03-01-002"));
			}
			// 建立服务会话
			session = Session.create(request);
			ExfTracker.start(Record.create(RecordType.SESSION, request.toString()));
			// 查询Service
			Service service = ServiceManager.getInstance().getService(request.getServiceCode(), request.getVersion());
			if (service == null) {
				throw new ExfException(
						ErrorMessage.code(true, "C-EXF-01-01-002", request.getServiceCode(), request.getVersion()));
			}
			// Container Link
			ContainerInst container = linkContainer(service);
			if (container == null) {
				return response;
			}
			// 身份识别
			identifyRecognize(session, service, request);

			session.getContext().fillContext(request);
			// 容器启动
			container.invoke();
			// 响应结果
			dealWithResponse(response, session);
		} catch (Throwable e) {
			LOG.error(String.format("exf Error. Req[%s] Detail[%s]", request, e.getMessage()), e);
			if (e instanceof ExfException) {
				dealWithExceptionResponse(response, (ExfException) e);
			} else if (e instanceof ExfRuntimeException) {
				dealWithExceptionResponse(response, new ExfException(e));
			} else {
				dealWithExceptionResponse(response, new ExfException(ErrorMessage.code(true, "C-EXF-01-01-004",
						request.getServiceCode(), request.getVersion(), request), e));
			}
		} finally {
			ExfTracker.success(response.isSuccess(), response.getErrMessage());
			ExfTracker.log(response);
			// 关闭会话
			if (null != session) {
				session.close();
			}
		}
		return response;
	}

	/**
	 * 身份识别及身份配置加载
	 * 
	 * @param session
	 *            会话
	 * @param service
	 *            目标服务
	 * @param request
	 *            请求对象（用于请求参数鉴别身份）
	 * @throws ExfException
	 * @throws MatchException
	 */
	private void identifyRecognize(Session session, Service service, ExfRequest request) throws ExfException {
		Identify identify = null;
		String serviceKey = ServiceManager.buildServiceKey(service.getCode(), service.getVersion());
		ExfTracker.start(Record.create(RecordType.IDENTITY, serviceKey));
		// 身份识别（按优先级高级识别）
		for (Identify supportIdentify : service.getSupportIdentifys()) {
			try {
				if (null != supportIdentify.getMatcher() && supportIdentify.getMatcher().match(request.getParams())) {
					identify = supportIdentify;
					break;
				}
			} catch (MatchException e) {
				LOG.error(String.format("Identify Match Error for Service[%s] Identify[%s]", serviceKey,
						supportIdentify.getId()), e);
				ExfTracker.success(false);
			}
		}
		if (null == identify) {
			ErrorMessage error = ErrorMessage.code(true, "C-EXF-01-01-003", serviceKey);
			ExfTracker.success(false, error.getErrorMessage());
			ExfTracker.stop();
			throw new ExfException(error);
		}
		session.setIdentify(identify.getId());
		// 加载身份配置
		session.getContext().fillContext(identify.getConfigs().get(serviceKey).getConfigs().values());
		ExfTracker.stop();
	}

	/**
	 * 处理响应结果
	 * 
	 * @param response
	 * @param session
	 */
	private void dealWithResponse(ExfResponse response, Session session) {
		ActionResult result = session.getContext().getContextResult();
		response.setResultCode(result.getState().getCode());
		if (!result.getState().isSuccess()) {
			response.setSuccess(false);
		}
		response.setResult(result.getModel());
	}

	/**
	 * 封装异常结果
	 * 
	 * @param response
	 * @param exception
	 */
	private void dealWithExceptionResponse(ExfResponse response, ExfException exception) {
		response.setSuccess(false);
		response.setResultCode(ActionState.ERROR.getCode());
		response.setErrCode(exception.getErrorMessage().getReadableErrorCode());
		response.setErrMessage(exception.getErrorMessage().getDisplayErrorMessage());
	}

	/**
	 * 链接容器
	 * 
	 * @param service
	 * @return
	 */
	private ContainerInst linkContainer(Service service) {
		ContainerInst container = null;
		if (null != service.getContainers() && service.getContainers().size() > 0) {
			for (ContainerInst instance : service.getContainers()) {
				if (null != container) {
					container.setNext(instance);
					container = instance;
				} else {
					container = instance;
				}
			}
			// 指针返回头结点
			container = service.getContainers().get(0);
		}
		return container;
	}

}
