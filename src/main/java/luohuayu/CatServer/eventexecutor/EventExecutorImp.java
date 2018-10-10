package luohuayu.CatServer.eventexecutor;

import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;

public abstract class EventExecutorImp implements EventExecutor{

    protected Class<?> mEventClass=null;

    @Override
    public final void execute(Listener pListener,Event pEvent) throws EventException{
        if(this.mEventClass==null||!this.mEventClass.isAssignableFrom(pEvent.getClass())){
            return;
        }

        try{
            this.invoke(pListener,pEvent);
        }catch(Throwable e){
            throw new EventException(e);
        }
    }

    public void initExecute(Class<?> pEventClass){
        this.mEventClass=pEventClass;
    }

    public abstract void invoke(Listener pListener,Event pEvent) throws Throwable;

}
