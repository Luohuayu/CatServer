package luohuayu.CatServer.eventexecutor;

import java.lang.reflect.Method;

import org.bukkit.event.Event;
import org.bukkit.event.Listener;

public class ReflectEventExecutor extends EventExecutorImp{

    private final Method mMethod;

    public ReflectEventExecutor(Method pMethod){
        this.mMethod=pMethod;
    }

    @Override
    public void invoke(Listener pListener,Event pEvent) throws Throwable{
        this.mMethod.invoke(pListener,pEvent);
    }

}
