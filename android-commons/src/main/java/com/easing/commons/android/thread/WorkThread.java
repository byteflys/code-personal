package com.easing.commons.android.thread;

import com.easing.commons.android.app.CommonApplication;
import com.easing.commons.android.app.CommonFragment;
import com.easing.commons.android.helper.callback.Action;
import com.easing.commons.android.helper.exception.BizException;
import com.easing.commons.android.helper.thread.ComponentState;
import com.easing.commons.android.app.CommonActivity;

import java.security.MessageDigest;

import lombok.SneakyThrows;

@SuppressWarnings("all")
public class WorkThread {

    //提交任务
    public static void post(String name, Action action) {
        Thread thread = new Thread(action::runAndPostException);
        thread.setName(handString(name));
        thread.start();
    }

    //提交任务
    public static void post(Action action) {
        String name = StackTrace.callerInfo();
        post(name, action);
    }

    private static SingleThreadPool pool;

    public static void initSingleThreadPool(Integer corePoolSize) {
        pool = SingleThreadPool.newInstance(corePoolSize);
    }

    /**
     * 提交任务
     *
     * @param isNewThread 是否新建线程
     */
    public static void post(Action action, boolean isNewThread) {

        if (isNewThread) {
            String name = StackTrace.callerInfo();
            post(name, action);
        } else {
            ThreadPoolExecutorUtil.getInstance().execute(action::runAndPostException);
        }
    }

    //延时提交任务
    @SneakyThrows
    public static void postLater(Action action, long ms) {
        String name = StackTrace.callerInfo();
        Thread thread = new Thread(() -> {
            Threads.sleep(ms);
            action.runAndPostException();
        });
        thread.setName(handString(name));
        thread.start();

        // ThreadPoolExecutor.getInstance().execute(action::runAndPostException);
        /*ThreadPoolExecutor.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                Threads.sleep(ms);
                action.runAndPostException();
            }
        });*/
    }

    //无限循环执行任务
    //可通过Threads.sleep(interval)设置执行间隔
    //可通过throw BizException.THREAD_NORMAL_EXIT退出
    @SneakyThrows
    public static void postByLoop(String name, Action action) {
        Thread thread = new Thread(() -> {
            while (true)
                try {
                    action.run();
                } catch (BizException e) {
                    if (e == BizException.THREAD_NORMAL_EXIT) break;
                    CommonApplication.ctx.handleGlobalException(e);
                } catch (Throwable e) {
                    CommonApplication.ctx.handleGlobalException(e);
                }
        });

        thread.setName(handString(name));
        thread.start();
    }

    public static String handString(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = s.getBytes();
            //获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            //使用指定的字节更新摘要
            mdInst.update(btInput);
            //获得密文
            byte[] md = mdInst.digest();
            //把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 尽量使用带名称的 postByLoop(String name, Action action)
     * 方便查找
     */
    @Deprecated
    public static void postByLoop(Action action) {
        String name = StackTrace.callerInfo();
        postByLoop(name, action);
    }

    //按一定时间间隔循环执行任务
    @SneakyThrows
    public static void postByInterval(String name, Action action, long interval, long delay) {
        Thread thread = new Thread(() -> {
            Threads.sleep(delay);
            while (true) {
                try {
                    action.run();
                    Threads.sleep(interval);
                } catch (BizException e) {
                    if (e == BizException.THREAD_NORMAL_EXIT)
                        break;
                    CommonApplication.ctx.handleGlobalException(e);
                } catch (Throwable e) {
                    CommonApplication.ctx.handleGlobalException(e);
                }
            }
        });
        thread.setName(handString(name));
        thread.start();
    }

    //按一定时间间隔循环执行任务，根据线程控制标记来停止
    @SneakyThrows
    public static void postByInterval(Action action, long interval, ThreadFlag flag) {
        String name = StackTrace.callerInfo();
        Thread thread = new Thread(() -> {
            while (flag.running) {
                try {
                    action.run();
                    Threads.sleep(interval);
                } catch (BizException e) {
                    if (e == BizException.THREAD_NORMAL_EXIT) break;
                    CommonApplication.ctx.handleGlobalException(e);
                } catch (Throwable e) {
                    CommonApplication.ctx.handleGlobalException(e);
                }
            }
        });
        thread.setName(handString(name));
        thread.start();
    }

    //按一定时间间隔循环执行任务，根据组件状态来停止
    @SneakyThrows
    public static void postByInterval(Action action, long interval, ComponentState state) {
        String name = StackTrace.callerInfo();
        Thread thread = new Thread(() -> {
            while (state.value != ComponentState.StateValue.DISPOSED) {
                try {
                    action.run();
                } catch (BizException e) {
                    if (e == BizException.THREAD_NORMAL_EXIT) break;
                    CommonApplication.ctx.handleGlobalException(e);
                } catch (Throwable e) {
                    CommonApplication.ctx.handleGlobalException(e);
                }
                Threads.sleep(interval);
            }
        });
        thread.setName(handString(name));
        thread.start();
    }

    //按一定时间间隔循环执行任务，根据Activity状态来停止
    @SneakyThrows
    public static void postByInterval(Action action, long interval, CommonActivity activity) {
        WorkThread.postByInterval(action, interval, activity.componentState);
    }

    //按一定时间间隔循环执行任务，根据Fragment状态来停止
    @SneakyThrows
    public static void postByInterval(Action action, long interval, CommonFragment fragment) {
        WorkThread.postByInterval(action, interval, fragment.componentState);
    }
}

