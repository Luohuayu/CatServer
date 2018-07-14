// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.util;

import java.net.URLConnection;
import java.io.InputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import com.google.common.base.Charsets;
import java.net.URL;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MojangNameLookup
{
    private static final Logger logger;
    
    static {
        logger = LogManager.getFormatterLogger((Class)MojangNameLookup.class);
    }
    
    public static String lookupName(final UUID id) {
        if (id == null) {
            return null;
        }
        InputStream inputStream = null;
        try {
            final URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + id.toString().replace("-", ""));
            final URLConnection connection = url.openConnection();
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);
            connection.setUseCaches(false);
            inputStream = connection.getInputStream();
            final String result = IOUtils.toString(inputStream, Charsets.UTF_8);
            final Gson gson = new Gson();
            final Response response = (Response)gson.fromJson(result, (Class)Response.class);
            if (response == null || response.name == null) {
                MojangNameLookup.logger.warn("Failed to lookup name from UUID");
                return null;
            }
            if (response.cause != null && response.cause.length() > 0) {
                MojangNameLookup.logger.warn("Failed to lookup name from UUID: %s", new Object[] { response.errorMessage });
                return null;
            }
            return response.name;
        }
        catch (MalformedURLException ex) {
            MojangNameLookup.logger.warn("Malformed URL in UUID lookup");
            return null;
        }
        catch (IOException ex2) {
            IOUtils.closeQuietly(inputStream);
        }
        finally {
            IOUtils.closeQuietly(inputStream);
        }
        return null;
    }
    
    private class Response
    {
        String errorMessage;
        String cause;
        String name;
    }
}
