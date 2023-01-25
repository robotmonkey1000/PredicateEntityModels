package studio.robotmonkey.predicatecustommodels.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class UtilManager {

    private static final Set<EntityType<? extends Entity>> supportedEntities      = new HashSet<>();
    private static final Set<String> supportedOthers      = new HashSet<>();
    private static final Logger LOGGER = LogManager.getLogger("Robots Custom Models");
    private static final Gson GSON = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();

    //Gson
    public static Gson getGson(){
        return GSON;
    }

    //renderer stuff
    public static boolean addSupport(EntityType<? extends Entity> entityType){
        return supportedEntities.add(entityType);
    }

    public static boolean addSupport(String id){
        return supportedOthers.add(id);
    }

    public static boolean isUnsupported(EntityType<? extends Entity> entityType){
        return !supportedEntities.contains(entityType);
    }

    public static boolean isUnsupported(String id){
        return !supportedOthers.contains(id);
    }


    public static String getEntityNameFromId(Identifier identifier){
        String id = identifier.toString();
        return id.substring(id.lastIndexOf("/") + 1, id.lastIndexOf(".rem"));
    }

    //file stuff
    public static Identifier transformPath(String path, Identifier location){
        //relative to current folder
        if(path.startsWith("./")){
            return new Identifier(location.getNamespace(), location.getPath().substring(0, location.getPath().lastIndexOf('/') + 1) + path.substring(2));
        }
        //go up a folder
        else if(path.startsWith("../")){
            return transformPath(path.substring(3), new Identifier(location.getNamespace(), location.getPath().substring(0, location.getPath().lastIndexOf('/'))));
        }
        //relative to "assets/robotmonkey/cem"
        else if(path.startsWith("~/")){
            return new Identifier("robotmonkey", "cem/" + path.substring(2));
        }
        //relative to "assets/namespace/"
        else if(path.chars().filter(ch -> ch == ':').count() == 1){
            String path2 = path.substring(path.indexOf(":") + 1);
            if(path2.startsWith("/")){
                path2 = path2.replaceFirst("/", "");
            }
            return transformPath(path2, new Identifier(path.substring(0, path.indexOf(":")), ""));
        }
        //look for file in current folder
        else{
            return new Identifier(location.getNamespace(), location.getPath().substring(0, location.getPath().lastIndexOf('/') + 1) + path);
        }
    }

    public static void postReadError(Exception exception, Identifier id){
        UtilManager.getLogger().error("Error parsing " + id + ":");
        String message = exception.getMessage();
        UtilManager.getLogger().error(exception);
        if(message == null || message.trim().equals("")){
            UtilManager.getLogger().error(exception.getStackTrace()[0]);
            UtilManager.getLogger().error(exception.getStackTrace()[1]);
            UtilManager.getLogger().error(exception.getStackTrace()[2]);
        }
    }

    //logger
    public static Logger getLogger(){
        return LOGGER;
    }

    //json
    public static Float JSONparseFloat(Object obj){
        String val = JSONparseString(obj);
        return val == null? null : Float.valueOf(val);
    }

    public static String JSONparseString(Object obj){
        return obj == null? null : obj.toString();
    }

    public static Boolean JSONparseBool(Object obj){
        String val = JSONparseString(obj);
        return val == null? null : Boolean.valueOf(val);
    }

    public static ArrayList<Double> JSONparseDoubleList(Object object){
        try{
            @SuppressWarnings("unchecked")
            ArrayList<Object> obj = (ArrayList<Object>) object;
            ArrayList<Double> val = new ArrayList<>();
            if(obj != null){
                obj.forEach((value) -> val.add(JSONparseDouble(value)));
            }
            return (val.size() == 0)? null : val;
        } catch(Exception e){
            return null;
        }
    }

    public static Double JSONparseDouble(Object obj){
        String val = JSONparseString(obj);
        return val == null? null : Double.valueOf(val);
    }

}
