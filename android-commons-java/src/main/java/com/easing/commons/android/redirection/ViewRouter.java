package com.easing.commons.android.redirection;

import android.app.Activity;
import android.view.View;

import com.easing.commons.android.annotation_processor.AnnotationHandler;
import com.easing.commons.android.app.CommonApplication;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("all")
public class ViewRouter {

    final static List<ViewRoutingRule> ruleList = new LinkedList();
    final static Map<String, ViewRoutingRule> ruleMap = new LinkedHashMap();

    public static ViewRoutingRule find(String ruleName) {
        return ruleMap.get(ruleName);
    }

    public static void init(ViewRoutingRule[] rules) {
        ruleList.clear();
        ruleMap.clear();
        for (ViewRoutingRule rule : rules) {
            ruleList.add(rule);
            ruleMap.put(rule.name, rule);
        }
    }

    public static void add(ViewRoutingRule[] rules) {
        for (ViewRoutingRule rule : rules)
            ViewRouter.add(rule);
    }

    public static void add(ViewRoutingRule rule) {
        ViewRoutingRule record = ruleMap.get(rule.name);
        if (record != null) ruleList.remove(record);
        ruleList.add(rule);
        ruleMap.put(rule.name, rule);
    }

    public static void initFromConfiguration(Class configuration) {
        ViewRoutingRule[] rules = AnnotationHandler.loadViewRoutingRule(configuration);
        ViewRouter.init(rules);
    }

    public static void addFromConfiguration(Class configuration) {
        ViewRoutingRule[] rules = AnnotationHandler.loadViewRoutingRule(configuration);
        ViewRouter.add(rules);
    }

    //查找@Redirection注解，并根据注解自动跳转
    public static void autoRedirect(View view, String ruleName) {
        if (ruleName == null) return;
        ViewRoutingRule rule = ViewRouter.find(ruleName);
        if (rule == null) return;
        CommonApplication.ctx.startActivity(rule.target);
        Activity activity = (Activity) view.getContext();
        if (rule.destroyCurrent) activity.finish();
    }

    //查找@Redirection注解，并根据注解自动跳转
    public static void autoRedirect(Activity activity, String ruleName) {
        ViewRoutingRule rule = ViewRouter.find(ruleName);
        if (rule == null) return;
        CommonApplication.ctx.startActivity(rule.target);
        if (rule.destroyCurrent) activity.finish();
    }

}
