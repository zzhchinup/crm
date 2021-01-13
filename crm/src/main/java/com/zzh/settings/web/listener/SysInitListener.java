package com.zzh.settings.web.listener;

import com.zzh.settings.domain.DicValue;
import com.zzh.settings.service.DicService;

import com.zzh.settings.service.impl.DicServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.List;
import java.util.Map;
import java.util.Set;


@WebListener
public class SysInitListener implements ServletContextListener {

    @Autowired
    private DicService dicService;
    //private static WebApplicationContext context;
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("服务器缓存处理数据字典开始");
        WebApplicationContext context =WebApplicationContextUtils.getRequiredWebApplicationContext(sce.getServletContext());
        context.getAutowireCapableBeanFactory().autowireBean(this);

        context = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());
        DicServiceImpl dicService = (DicServiceImpl) context.getBean("dicServiceImpl");

        ServletContext application = sce.getServletContext();

        // 取数据字典。
        Map<String, List<DicValue>> map = dicService.getAll();

        Set<String> set = map.keySet();
        // 将map中的键值对保存为域对象的键值对。
        for (String key:set){
            application.setAttribute(key,map.get(key));
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

        System.out.println("服务器缓存处理数据字典结束");
    }
}
