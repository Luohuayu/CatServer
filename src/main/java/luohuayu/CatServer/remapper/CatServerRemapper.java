package luohuayu.CatServer.remapper;

import static org.objectweb.asm.ClassWriter.COMPUTE_MAXS;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import net.md_5.specialsource.JarMapping;
import net.md_5.specialsource.JarRemapper;
import net.md_5.specialsource.RemappingClassAdapter;
import net.md_5.specialsource.SpecialSource;
import net.md_5.specialsource.repo.CachingRepo;
import net.md_5.specialsource.repo.ClassRepo;
import net.md_5.specialsource.repo.RuntimeRepo;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class CatServerRemapper extends JarRemapper{

    private int writerFlags = COMPUTE_MAXS;
    private int readerFlags = 0;
    private boolean copyResources = true;
    
    private final Map<String, ClassNode> mCache;
    
    public CatServerRemapper(JarMapping jarMapping){
        super(jarMapping);
        
       this.mCache=ReflectionHelper.getPrivateValue(CachingRepo.class,RuntimeRepo.getInstance(),"cache");
    }

    public String mapSignature(String signature, boolean typeSignature) {
        try {
            return super.mapSignature(signature, typeSignature);
        } catch (Exception e) {
            return signature;
        }
    }

    @Override
    public byte[] remapClassFile(InputStream is,ClassRepo repo) throws IOException{
        return remapClassFileCC(new ClassReader(is),repo);
    }

    @Override
    public byte[] remapClassFile(byte[] in,ClassRepo repo){
        return remapClassFileCC(new ClassReader(in),repo);
    }

    @SuppressWarnings("unchecked")
    private byte[] remapClassFileCC(ClassReader reader,final ClassRepo repo){
        ClassNode node=new ClassNode();
        RemappingClassAdapter mapper=new RemappingClassAdapter(node,this,repo);
        String tKey=reader.getClassName();
        this.mCache.put(tKey,node);
        reader.accept(mapper,readerFlags);
        this.mCache.remove(tKey,node);

        ClassWriter wr=new ClassWriter(writerFlags);
        node.accept(wr);
        if(SpecialSource.identifier!=null){
            wr.newUTF8(SpecialSource.identifier);
        }

        return wr.toByteArray();
    }
    
    @Override
    public void setGenerateAPI(boolean generateAPI) {
        if (generateAPI) {
            readerFlags |= ClassReader.SKIP_CODE;
            copyResources = false;
        } else {
            readerFlags &= ~ClassReader.SKIP_CODE;
            copyResources = true;
        }
        super.setGenerateAPI(generateAPI);
    }
}
