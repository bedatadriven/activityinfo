/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Portions derived from google-web-toolkit and gwt-gears are probably copyright
 * Google. The rest copyright 2009 Alex Bertram and ActivityInfo contributors.
 */
package org.activityinfo.linker;

import com.google.gwt.core.ext.LinkerContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.linker.*;
import com.google.gwt.core.ext.linker.LinkerOrder.Order;
import com.google.gwt.core.linker.IFrameLinker;
import com.google.gwt.dev.util.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Date;

/**
 * Extends the standard <code>IFrameLinker</code> to generate manifests for
 * <code>ManagedResourceStores</code>.
 *
 * In this sense, the implementation is similar to (and based on) the
 *  {@link com.google.gwt.gears.offline.linker.GearsManifestLinker} shipped with the
 * GWT-Gears package, but, crucially, creates two sets of manifests:
 * <ul>
 * <li>The <strong>common manifest</strong>, which always has the name
 * <code>moduleName.nocache.manifest</code> and is versioned with the compilation time, but
 * does not contain any javascript beyond the selection script.</li>
 * <li><strong>Permutation manifests</strong> are emitted for each individual CompilationResult,
 * so that clients only need to download their scripts. Each the Application</li>
 * </ul>
 * 
 *
 */
@LinkerOrder(Order.PRIMARY)
public final class ApplicationLinker extends IFrameLinker {


    public ApplicationLinker() {
    }

    @Override
    public String getDescription() {
        return "ActivityInfo Linker";
    }

    @Override
    public ArtifactSet link(TreeLogger logger, LinkerContext context,
                            ArtifactSet artifacts) throws UnableToCompleteException {

        logger = logger.branch(TreeLogger.DEBUG, "Linking ActivityInfo with the ApplicationLinker");

        // before we start linking compiliation results, generate the common manifest
        // which will contain the host page, selection script, and any other content
        // found in the public/ folder
        Artifact commonManifest = emitCommonManifest(logger, context, artifacts);

        // let the IFrameLinker do its work
        ArtifactSet toReturn = super.link(logger, context, artifacts);
        toReturn.add(commonManifest);

        return toReturn;
    }

    private Artifact emitCommonManifest(TreeLogger logger, LinkerContext context, ArtifactSet artifacts)
            throws UnableToCompleteException {

        logger = logger.branch(TreeLogger.DEBUG, "Generating common manifest contents",
            null);

        StringBuffer out = readManifestTemplate(logger, "Common.manifest");
        
        // Generate the manifest entries
        StringBuffer entries = generateEntries(logger, context,
                artifacts.find(EmittedArtifact.class));

        // Add the selection script
        entries.append(",\n{ \"url\" : \"").append(context.getModuleName())
                .append(".nocache.js\" }");

        // Add an alias for Module.nocache.js?compiled to support hosted-mode
        entries.append(",\n{ \"url\" : \"").append(context.getModuleName())
                .append(".nocache.js?compiled\" }");

        // use the current time as the version number
        replaceAll(out, "__VERSION__", Long.toHexString((new Date()).getTime()));
        replaceAll(out, "__ENTRIES__", entries.toString());

       /*
        * NB: It's tempting to use LinkerContext.optimizeJavaScript here, but the
        * JSON standard requires that the keys in the object literal will be
        * enclosed in double-quotes. In our optimized JS form, the double-quotes
        * would normally be removed.
        */
        return emitBytes(logger, Util.getBytes(out.toString()),
                context.getModuleName() + ".nocache.manifest");
    }

    /**
     * Hooks into the emission of a CompilationResult and saves a manifest
     * for this particular permutation
     *
     */
    @Override
    protected Collection<EmittedArtifact> doEmitCompilation(TreeLogger logger,
                   LinkerContext context, CompilationResult compilationResult)
            throws UnableToCompleteException {

        Collection<EmittedArtifact> artifacts = super.doEmitCompilation(logger, context, compilationResult);

        logger = logger.branch(TreeLogger.DEBUG, "Generating manifest for permutation " +
                compilationResult.getPermutationId(), null);

        // generate a manifest for this permutation
        StringBuffer out = readManifestTemplate(logger, "Permutation.manifest");

       // Generate the manifest entries
       StringBuffer entries = generateEntries(logger, context, artifacts);

       // use the current time as the version number
        replaceAll(out, "__ENTRIES__", entries.toString());

        artifacts.add(emitBytes(logger, Util.getBytes(out.toString()),
                context.getModuleName() + "." + compilationResult.getStrongName() + ".cache.manifest"));

        return artifacts;
    }

    /**
     * Generate a string containing object literals for each manifest entry.
     */
    private StringBuffer generateEntries(TreeLogger logger, LinkerContext context,
                                   Collection<EmittedArtifact> artifacts)
            throws UnableToCompleteException {

         logger = logger.branch(TreeLogger.DEBUG, "Generating manifest entries",
            null);

        StringBuffer entries = new StringBuffer();
        boolean needsComma = false;
        for (EmittedArtifact artifact : artifacts) {
            if (artifact.isPrivate()) {
                logger.log(TreeLogger.DEBUG, "excluding private: " + artifact.getPartialPath());

                // These artifacts won't be in the module output directory
                continue;
            }

            String path = artifact.getPartialPath();

            // certain paths on the Windows platform (notably deferredjs stuff)
            // show up with backslahes, which throws an illegal escape sequence
            // error when the json is parsed.
            path = path.replace('\\', '/');

            logger.log(TreeLogger.DEBUG, "adding: " + path);

            if (needsComma) {
                entries.append(",\n");
            } else {
                needsComma = true;
            }

            entries.append("{ \"url\" : \"");
            entries.append(path);
            entries.append("\" }");
        }
        return entries;
    }

    /**
     * Load the contents of the manifest template resource. Exposed as public for testing.
     * @param logger TreeLogger
     * @param name File name of the template (should be in the package org.activityinfo.linker)
     * @return the text of the template file
     * @throws com.google.gwt.core.ext.UnableToCompleteException if something goes wrong with I/O
     */
    public StringBuffer readManifestTemplate(TreeLogger logger,
                                              String name) throws UnableToCompleteException {
        logger = logger.branch(TreeLogger.DEBUG, "Reading manifest template", null);

        String resourceName = "/manifest/" + name;
        InputStream in = getClass().getResourceAsStream(resourceName);
        if (in == null) {
            logger.log(TreeLogger.ERROR, "Could not load manifest template from "
                    + resourceName, null);
            throw new UnableToCompleteException();
        }

        StringBuffer out = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        try {
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                out.append(line).append("\n");
            }
        } catch (IOException e) {
            logger.log(TreeLogger.ERROR, "Unable to read manifest template", e);
            throw new UnableToCompleteException();
        }

        return out;
    }
}
