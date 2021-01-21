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
import java.util.*;


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

        //-------------------------------------------------------------------------------------------
        // 数据字典处理完毕时候，处理Stage2Possibility.properties文件
        /*
            处理Stage2Possibility.properties文件步骤：
                解析该文件，将该属性文件中的键值对关系处理称为java中的键值对关系(map)
                Map<String(阶段),String(可能性)> pMap = ...
                pMap.put("01资质审查",10);
                pMap.put("02需求分析",25);
                pMap.put("03.."...);

                pMap保存值之后，放在服务器缓存中。
                application.setAttribute("pMap",pMap)
         */
        Map<String,String> pMap = new HashMap<>();
        ResourceBundle rb = ResourceBundle.getBundle("conf/Stage2Possibility");
        Enumeration<String>  enumeration = rb.getKeys();
        while (enumeration.hasMoreElements()){
            // 阶段
            String key = enumeration.nextElement();
            // 可能性
            String value = rb.getString(key);
            pMap.put(key,value);
        }
        application.setAttribute("pMap",pMap);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

        System.out.println("服务器缓存处理数据字典结束");
    }
}
