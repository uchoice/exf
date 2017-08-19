package net.uchoice.exf.core.loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import javax.annotation.Nonnull;

import com.google.common.collect.Sets;
import net.uchoice.exf.client.exception.ErrorMessage;
import net.uchoice.exf.client.exception.ExfException;
import net.uchoice.exf.core.loader.cache.PluginCache;
import net.uchoice.exf.core.loader.parser.PluginParser;
import net.uchoice.exf.core.util.ClassLoaderUtil;
import net.uchoice.exf.core.util.ClassPathScanHandler;
import net.uchoice.exf.model.action.ActionInst;
import net.uchoice.exf.model.action.ActionSpec;
import net.uchoice.exf.model.action.annotation.Action;
import net.uchoice.exf.model.container.ContainerInst;
import net.uchoice.exf.model.container.ContainerSpec;
import net.uchoice.exf.model.container.annotation.Container;
import net.uchoice.exf.model.plugin.Plugin;
import net.uchoice.exf.model.variable.VariableMode;
import net.uchoice.exf.model.variable.VariableSpec;
import net.uchoice.exf.model.variable.annotation.Prop;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;

/**
 * 插件管理器
 * 
 */
public class PluginManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(PluginManager.class);
	/**
	 * 插件文件
	 */
	private static final String PLUGIN_FILE = "exf-plugin.xml";
	/**
	 * 插件管理器实例
	 */
	private static volatile PluginManager instance = null;
	/**
	 * 是否开始初始化
	 */
	private static volatile boolean initialized = false;
	/**
	 * 是否已经初始化完成
	 */
	private static volatile boolean finished = false;
	/**
	 * 插件文件所在路径
	 */
	private Set<String> pluginDirs = new HashSet<String>();
	/**
	 * 插件缓存
	 */
	private PluginCache pluginCache = new PluginCache();

	/**
	 * 插件文件路径URL
	 */
	private URL[] pluginFileURLs = ClassLoaderUtil.getResources(PLUGIN_FILE);

	/**
	 * 插件key对应的插件实例
	 */
	private Map<String, Plugin> pluginMap = new HashMap<>();
	/**
	 * 插件文件全路径
	 */
	private Set<String> pluginFilePaths = new HashSet<>();

	/**
	 * 获取插件管理器实例
	 * 
	 * @return
	 * @throws ExfException
	 */
	public static PluginManager getInstance() throws ExfException {
		PluginManager pluginManager = instance;
		if (pluginManager == null) {
			synchronized (PluginManager.class) {
				pluginManager = instance;
				if (pluginManager == null) {
					pluginManager = new PluginManager();
					try {
						pluginManager.init();
					} catch (Throwable e) {
						throw new ExfException(ErrorMessage.code("P-EXF-01-01-001"), e);
					}
					instance = pluginManager;
				}
			}
		}
		return pluginManager;
	}

	/**
	 * 初始化
	 * 
	 * @throws Exception
	 */
	public void init() throws Exception {
		synchronized (this) {
			// 如果已经开始初始化但又未初始化完成，等待1000ms,这里设置1000ms前提在1000ms内初始化肯定会完成
			while (initialized && !finished) {
				wait(1000);
			}
			// 如果已经开始初始化并且初始化完成，直接返回
			if (initialized && finished) {
				return;
			}

			// 如果未开始初始化，执行初始化操作
			if (!initialized) {
				// 设置初始化标识
				initialized = true;
				LOGGER.info(">>>>>>[exf] PluginManager afterPropertiesSet, loading plugins...");
				// 获取插件管理器jar全路径
				String jarFilePath = PluginManager.class.getProtectionDomain().getCodeSource().getLocation().getFile();
				// 对特殊字符及中文编码
				File path = new File(jarFilePath);
				try {
					jarFilePath = java.net.URLDecoder.decode(path.getPath(), "UTF-8");
				} catch (UnsupportedEncodingException e) {
					LOGGER.error(e.getMessage(), e);
				}
				// 获取插件管理器jar所在文件目录
				int idx = jarFilePath.lastIndexOf(File.separator);
				String libPath = jarFilePath.substring(0, idx);
				LOGGER.info(">>>>>>[exf] load plugins from lib path: " + libPath);
				this.init(new String[] { libPath });
				// 初始化完成标识
				finished = true;
			}
		}
	}

	/**
	 * 初始化
	 * 
	 * @param pluginDirs
	 */
	public void init(@Nonnull String[] pluginDirs) {
		List<String> updatedDir = new ArrayList<>();
		for (String path : pluginDirs) {
			if (this.pluginDirs.contains(path)) {
				continue;
			}
			updatedDir.add(path);
		}

		for (String dir : updatedDir) {
			scanDirectoryAndLoadPlugins(dir);
			this.pluginDirs.add(dir);
		}
	}

	/**
	 * 扫描目录并加载插件
	 * 
	 * @param directory
	 */
	private void scanDirectoryAndLoadPlugins(String directory) {
		if (StringUtils.isEmpty(directory)) {
			return;
		}
		File dir = new File(directory);
		if (dir.isFile()) {
			// 是文件，则返回该文件的目录
			dir = dir.getParentFile();
		}
		if (null == dir || !dir.isDirectory()) {
			// 如果不是目录,则可能是Jar包的URL,从URL中读取配置
			loadPluginFileFromJarURL(directory);
		} else if (dir.isDirectory()) {
			// jar资源加载
			loadJarFilesInDirectory(dir);
			// 目录下文件资源加载
			loadPluginDescriptionFileInDirectory(dir);
		}
		// 最后merge下通过ClassPath扫到的Plugin
		loadFromClassPath(pluginFileURLs);

	}

	/**
	 * 加载配置文件
	 * 
	 * @param pluginFileURLs
	 */
	private void loadFromClassPath(URL[] pluginFileURLs) {
		for (URL url : pluginFileURLs) {
			try {
				String context = getPluginContextFromURL(url);
				if (StringUtils.isNotBlank(context)) {
					pluginFilePaths.add(url.getPath());
					Plugin plugin = PluginParser.parse(context);
					if (pluginMap.containsKey(plugin.getKey())) {
						LOGGER.info(">>>>>>[exf] pluginMap contain the key [" + plugin.getKey() + "] when loading from "
								+ url.getPath());
						continue;
					}
					initPlugin(plugin);
				}
			} catch (Throwable e) { // 如果有失败，还需要继续加载，不要影响其他业务包
				LOGGER.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * 加载jar文件
	 * 
	 * @param dir
	 */
	private void loadJarFilesInDirectory(File dir) {
		// 获取目录下的所有jar文件
		File[] jarFiles = dir.listFiles((file, name) -> name.toLowerCase().endsWith(".jar"));
		if (null == jarFiles || jarFiles.length == 0) {
			return;
		}
		for (File file : jarFiles) {
			JarFile jarFile = null;
			try {
				// 获取指定的插件文件
				jarFile = new JarFile(file);
				ZipEntry zipEntry = jarFile.getEntry(PLUGIN_FILE);
				if (null == zipEntry) {
					continue;
				}
				LOGGER.info(">>>>>>[exf] found " + PLUGIN_FILE + " in " + jarFile.getName());
				InputStream is = jarFile.getInputStream(zipEntry);
				if (null == is) {
					LOGGER.info(">>>>>>[exf] unable to get input stream from the zip entry." + jarFile.getName());
					continue;
				}
				pluginDirs.add(file.getAbsolutePath());
				pluginFilePaths.add(file.getAbsolutePath());
				Plugin plugin = PluginParser.parse(is);
				if (pluginMap.containsKey(plugin.getKey())) {
					LOGGER.info(">>>>>>[exf] pluginMap contain the key [" + plugin.getKey() + "] when loading from "
							+ jarFile.getName());
					continue;
				}
				initPlugin(plugin);
			} catch (Throwable th) { // 如果有失败，还需要继续加载，不要影响其他业务包
				LOGGER.error(th.getMessage(), th);
			} finally {
				if (null != jarFile) {
					try {
						jarFile.close();
					} catch (IOException e) {
						LOGGER.error(e.getMessage(), e);
					}
				}
			}
		}
	}

	private void loadPluginDescriptionFileInDirectory(File dir) {
		File[] pluginFiles = dir.listFiles((file, name) -> name.toLowerCase().equals(PLUGIN_FILE));
		if (null == pluginFiles || pluginFiles.length == 0) {
			return;
		}
		for (File file : pluginFiles) {
			try {
				String text = getTextFromFile(file);
				if (StringUtils.isEmpty(text)) {
					continue;
				}

				pluginFilePaths.add(file.getAbsolutePath());
				Plugin plugin = PluginParser.parse(text);
				if (pluginMap.containsKey(plugin.getKey())) {
					LOGGER.info(">>>>>>[exf] pluginMap contain the key [" + plugin.getKey() + "] when loading from "
							+ file.getAbsolutePath());
					continue;
				}
				initPlugin(plugin);
			} catch (Throwable e) { // 如果有失败，还需要继续加载，不要影响其他业务包
				LOGGER.error(e.getMessage(), e);
			}
		}
	}

	private void loadPluginFileFromJarURL(String directory) {
		for (URL url : pluginFileURLs) {
			if (url.getPath().equals(directory)) {
				try {
					String context = getPluginContextFromURL(url);
					if (StringUtils.isNotBlank(context)) {
						pluginFilePaths.add(url.getPath());
						Plugin plugin = PluginParser.parse(context);
						if (pluginMap.containsKey(plugin.getKey())) {
							LOGGER.info(">>>>>>[exf] pluginMap contain the key [" + plugin.getKey()
									+ "] when loading from " + url.getPath());
							continue;
						}
						initPlugin(plugin);
					}
				} catch (Throwable e) { // 如果有失败，还需要继续加载，不要影响其他业务包
					LOGGER.error(e.getMessage(), e);
				}
				break;
			}
		}
	}

	/**
	 * 初始化插件
	 * 
	 * @param plugin
	 * @throws ExfException
	 */
	private void initPlugin(Plugin plugin) throws ExfException {
		if (pluginMap.containsKey(plugin.getKey())) {
			// 如果已经有同key插件存在，则忽略
			return;
		}
		try {
			LOGGER.info(String.format("Loading plugin: [%s]", plugin.getKey()));

			pluginMap.put(plugin.getKey(), plugin);

			registerSpec(plugin.getKey(), plugin.getAllPackages());

		} catch (Throwable th) {
			throw new ExfException(ErrorMessage.code("P-EXF-01-01-002", plugin), th);
		}
	}

	private String getPluginContextFromURL(URL url) throws ExfException {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"))) {
			String line;
			StringBuilder context = new StringBuilder();
			while ((line = br.readLine()) != null) {
				context.append(line);
			}
			return context.toString();
		} catch (Exception e) {
			throw new ExfException(ErrorMessage.code("P-EXF-01-01-003", url.getPath()), e);
		}
	}

	private String getTextFromFile(File file) throws ExfException {
		if (null == file) {
			return StringUtils.EMPTY;
		}
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			return getTextFromInputStream(is);
		} catch (Throwable e) {
			throw new ExfException(ErrorMessage.code("P-EXF-01-01-003", file.getPath()), e);
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
		}
	}

	private String getTextFromInputStream(InputStream inputStream) throws IOException {
		if (null == inputStream) {
			return StringUtils.EMPTY;
		}
		StringBuffer buffer = new StringBuffer();
		int len = inputStream.available();
		while (len > 0) {
			byte[] bytes = new byte[len];
			inputStream.read(bytes);
			buffer.append(new String(bytes, "UTF-8"));
			len = inputStream.available();
		}
		return buffer.toString();
	}

	/***************************************
	 * Plugin load End
	 **************************************************/

	/**
	 * 加载容器及Action
	 * 
	 * @param pluginKey
	 * @param classPackages
	 * @return
	 */
	public void registerSpec(String pluginKey, String... classPackages) {
		ClassPathScanHandler handler = new ClassPathScanHandler();
		Set<Class<?>> classSet = Sets.newHashSet();
		// 加载指定package下的所有java文件
		for (String classPackage : classPackages) {
			classSet.addAll(handler.getPackageAllClasses(classPackage, true));
		}

		for (Class<?> targetClass : classSet) {
			registContainerSpec(pluginKey, targetClass);
			registActionSpec(pluginKey, targetClass);
		}
	}

	/**
	 * 注册容器定义
	 * 
	 * @param pluginKey
	 * @param targetClass
	 */
	@SuppressWarnings({ "unchecked" })
	private void registContainerSpec(String pluginKey, Class<?> targetClass) {
		if (null == targetClass) {
			return;
		}
		// 检查加载的class是否有容器标识
		Container container = targetClass.getDeclaredAnnotation(Container.class);
		if (null == container || !ContainerInst.class.isAssignableFrom(targetClass)) {
			return;
		}
		// 构建容器定义并缓存容器定义
		ContainerSpec containerSpec = ContainerSpec.of(pluginKey, container.code(), container.name(),
				(Class<? extends ContainerInst>) targetClass);
		if (StringUtils.isNotBlank(container.desc())) {
			containerSpec.setDesc(container.desc());
		}
		getPluginCache().cacheContainerSpec(containerSpec);
	}

	/**
	 * 注册action定义
	 * 
	 * @param pluginKey
	 * @param targetClass
	 */
	@SuppressWarnings("unchecked")
	private void registActionSpec(String pluginKey, Class<?> targetClass) {
		if (null == targetClass) {
			return;
		}
		// 检查加载的class是否有action标识
		Action action = targetClass.getDeclaredAnnotation(Action.class);
		if (null == action || !ActionInst.class.isAssignableFrom(targetClass)) {
			return;
		}
		Method method = null;
		// 获取action声明的方法
		Method[] methods = targetClass.getDeclaredMethods();
		if (null == methods) {
			return;
		}
		// 判断action声明的方法是否action实际有提供
		for (Method m : methods) {
			if (m.getName().equals(action.method())) {
				method = m;
				break;
			}
		}
		if (null == method) {
			return;
		}
		// 构建action定义，按这里的写法，只支持声明一个方法并且action只能提供一个方法
		ActionSpec actionSpec = ActionSpec.of(pluginKey, action.code(), action.name(),
				(Class<? extends ActionInst>) targetClass, method);

		// 扫描参数变量
		scanInVariable(actionSpec);
		// 扫描属性变量
		scanPropVariable(actionSpec);
		// 缓存action定义
		getPluginCache().cacheActionSpec(actionSpec);
	}

	/**
	 * 扫描ACTION参数变量
	 * 
	 * @param actionSpec
	 */
	private void scanInVariable(ActionSpec actionSpec) {
		Method method = actionSpec.getMethod();
		String[] paramsNames = new LocalVariableTableParameterNameDiscoverer().getParameterNames(method);
		Parameter[] params = method.getParameters();
		if (null != params && null != paramsNames && params.length == paramsNames.length && params.length > 0) {
			VariableSpec inVariabbleSpec = null;
			for (int index = 0; index < params.length; index++) {
				inVariabbleSpec = VariableSpec.of(VariableMode.IN, paramsNames[index], params[index].getType(), index);
				inVariabbleSpec.setGenerateType(params[index].getParameterizedType());
				actionSpec.addVariable(inVariabbleSpec);
			}
		}
	}

	/**
	 * 扫描ACTION 属性变量
	 * 
	 * @param actionSpec
	 */
	private void scanPropVariable(ActionSpec actionSpec) {
		Field[] fields = actionSpec.getType().getDeclaredFields();
		VariableSpec propVariabbleSpec = null;
		if (null != fields && fields.length > 0) {
			for (int index = 0; index < fields.length; index++) {
				if (fields[index].isAnnotationPresent(Prop.class)) {
					Prop prop = fields[index].getAnnotation(Prop.class);
					propVariabbleSpec = VariableSpec.of(VariableMode.PROP, fields[index].getName(),
							fields[index].getType(), index, prop.required());
					propVariabbleSpec.setGenerateType(fields[index].getGenericType());
					actionSpec.addVariable(propVariabbleSpec);
				}
			}
		}
	}

	public static boolean isFinished() {
		return finished;
	}

	public Set<String> getPluginDirs() {
		return pluginDirs;
	}

	public PluginCache getPluginCache() {
		return pluginCache;
	}

	public Map<String, Plugin> getPluginMap() {
		return pluginMap;
	}

	public void setPluginMap(Map<String, Plugin> pluginMap) {
		this.pluginMap = pluginMap;
	}

	public ContainerSpec getContainerSpec(String code) {
		return getPluginCache().getCachedContainerSpec(code);
	}

	public ActionSpec getActionSpec(String code) {
		return getPluginCache().getCachedActionSpec(code);
	}
}
