package net.uchoice.exf.core.context;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.uchoice.exf.client.service.ExfRequest;
import net.uchoice.exf.core.identify.IdentifyConfig;
import net.uchoice.exf.model.action.ActionResult;
import org.apache.commons.lang.StringUtils;

public class Context {

	/**
	 * request参数变量UID
	 */
	public static final String REQUEST_UID_KEY = "_request";

	public static final String CONFIG_UID_KEY = "_config";

	public static final String ACTION_RESULT_KEY = "_result";

	private Map<String/* 对象名 */, Map<String/* 节点实例名 */, Object/* 对象 */>> context = new HashMap<>();

	// 取最后一个节点执行结果
	private ActionResult contextResult = null;

	public void fillContext(String uid, ActionResult actionResult) {
		if (null != actionResult && StringUtils.isNotBlank(uid)) {
			Map<String, Object> model = actionResult.getModel();
			if (!model.isEmpty()) {
				for (String key : model.keySet()) {
					Map<String, Object> valueMap = context.get(key);
					if (null == valueMap) {
						valueMap = new HashMap<>();
						context.put(key, valueMap);
					}
					valueMap.put(uid, model.get(key));
				}
			}
			if (null != actionResult.getResult()) {
				Map<String, Object> valueMap = context.get(ACTION_RESULT_KEY);
				if (null == valueMap) {
					valueMap = new HashMap<>();
					context.put(ACTION_RESULT_KEY, valueMap);
				}
				valueMap.put(uid, actionResult.getResult());
			}
		}
	}

	/**
	 * 请求参数填充至上下文
	 * 
	 * @param request
	 */
	public void fillContext(ExfRequest request) {
		if (null != request && null != request.getParams()) {
			Map<String, Object> params = request.getParams();
			if (!params.isEmpty()) {
				for (String key : params.keySet()) {
					Map<String, Object> valueMap = context.get(key);
					if (null == valueMap) {
						valueMap = new HashMap<>();
						context.put(key, valueMap);
					}
					valueMap.put(REQUEST_UID_KEY, params.get(key));
				}
			}
		}
	}

	/**
	 * 填充身份配置点
	 * 
	 * @param configs
	 */
	public void fillContext(Collection<IdentifyConfig> configs) {
		if (null != configs && !configs.isEmpty()) {
			for (IdentifyConfig config : configs) {
				Map<String, Object> valueMap = context.get(config.getId());
				if (null == valueMap) {
					valueMap = new HashMap<>();
					context.put(config.getId(), valueMap);
				}
				valueMap.put(CONFIG_UID_KEY, config.getValue());
			}
		}
	}

	public Object getValue(String name, String uid) {
		Object value = null;
		if (StringUtils.isBlank(name)) {
			return value;
		}
		Map<String, Object> objects = context.get(name);
		if (null != objects) {
			if (objects.size() > 1 && StringUtils.isBlank(uid)) {
				throw new RuntimeException(
						String.format("Multiple Object [%s] find in context,uid must be specified.", name));
			}
			if (objects.size() == 1) {
				for (String key : objects.keySet()) {
					value = objects.get(key);
				}
			} else {
				value = objects.get(uid);
			}
		}
		return value;
	}

	/**
	 * 导出上下文Map 用于变量取值
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String/* uid.name或者name */, Object> extractContext() {
		Map<String, Object> map = new HashMap<>();
		for (String name : context.keySet()) {
			if (null != context.get(name)) {
				// 变量只有一个实例时可以直接通过变量名获取
				if (context.get(name).size() == 1) {
					context.get(name).forEach((k, v) -> {
						map.put(name, v);
					});
				}
				context.get(name).forEach((k, v) -> {
					if (map.get(k) != null && !(map.get(k) instanceof Map)) {
						return;
					}
					Map<String, Object> valMap = (Map<String, Object>) map.get(k);
					if (null == valMap) {
						valMap = new HashMap<>();
					}
					valMap.put(name, v);
					map.put(k, valMap);
				});
			}
		}
		return map;
	}

	public Map<String, Map<String, Object>> getContext() {
		return context;
	}

	public void setContext(Map<String, Map<String, Object>> context) {
		this.context = context;
	}

	public ActionResult getContextResult() {
		return contextResult;
	}

	public void setContextResult(ActionResult contextResult) {
		this.contextResult = contextResult;
	}
}
