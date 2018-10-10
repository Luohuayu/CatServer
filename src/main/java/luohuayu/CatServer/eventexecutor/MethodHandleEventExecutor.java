package luohuayu.CatServer.eventexecutor;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.bukkit.event.Event;
import org.bukkit.event.Listener;

/**
 * @author paper
 */
public class MethodHandleEventExecutor extends EventExecutorImp{

    private static Constructor<Lookup> mCons=null;
    private MethodHandle mHandle;

    public MethodHandleEventExecutor(Object pTarget,Method pMethod){
        try{
            pMethod.setAccessible(true);
            this.mHandle=newLookup(pMethod.getDeclaringClass()).unreflect(pMethod);
            if(!Modifier.isStatic(pMethod.getModifiers())){
                this.mHandle=this.mHandle.bindTo(pTarget);
            }
        }catch(IllegalAccessException e){
            throw new AssertionError("Unable to set accessible",e);
        }
    }

    @Override
    public void invoke(Listener pListener,Event pEvent) throws Throwable{
        this.mHandle.invoke(pEvent);
    }

    public static Lookup newLookup(Class<?> pTarget){
        try{
            synchronized(MethodHandleEventExecutor.class){
                if(mCons==null){
                    mCons=Lookup.class.getDeclaredConstructor(Class.class);
                    mCons.setAccessible(true);
                }
            }

            return mCons.newInstance(pTarget);
        }catch(Throwable exp){
            throw new IllegalStateException(exp);
        }
    }
}
