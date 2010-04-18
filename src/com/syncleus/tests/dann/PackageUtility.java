package com.syncleus.tests.dann;

import java.net.URL;
import java.util.jar.*;
import java.util.*;
import java.io.*;

public abstract class PackageUtility
{
	public static Class[] getClasses(String pkgName) throws ClassNotFoundException
	{
		ArrayList<Class> classes = new ArrayList<Class>();
		// Get a File object for the package
		File directory = null;
		String pkgPath = null;
		try
		{
			ClassLoader cld = Thread.currentThread().getContextClassLoader();
			if (cld == null)
			{
				throw new ClassNotFoundException("Can't get class loader.");
			}

			pkgPath = pkgName.replace('.', '/');
			URL resource = cld.getResource(pkgPath);
			if (resource == null)
			{
				throw new ClassNotFoundException("No resource for " + pkgPath);
			}
			directory = new File(resource.getFile());
		}
		catch (NullPointerException x)
		{
			throw new ClassNotFoundException(pkgName + " (" + directory + ") does not appear to be a valid package");
		}
		
		
		if (directory.exists())
		{
			// Get the list of the files contained in the package
			String[] files = directory.list();
			for (int i = 0; i < files.length; i++)
			{
				// we are only interested in .class files
				if (files[i].endsWith(".class"))
				{
					// removes the .class extension
					classes.add(Class.forName(pkgName + '.' + files[i].substring(0, files[i].length() - 6)));
				}
			}
			
			Class[] classesA = new Class[classes.size()];
			classes.toArray(classesA);
			return classesA;
		}
		else
		{
			//first clean it up in case wer on *nix system
			String jarPath = directory.toString().replace("!/" + pkgPath, "").replace("file:", "");
			//now clean up for windows
			jarPath = jarPath.replace("!\\" + pkgPath.replace("/", "\\"), "").replace("file:", "");
			try
			{
				return PackageUtility.getClasses(jarPath, pkgName);
			}
			catch(Exception caughtException)
			{
				caughtException.printStackTrace();
				throw new InternalError("Unexpected internal error!");
			}
		}
	}
	
	public static Class[] getClasses(String jarName, String packageName) throws FileNotFoundException, IOException
	{
		ArrayList<Class> classes = new ArrayList<Class>();
		
		packageName = packageName.replaceAll("\\." , "/");
		
		JarInputStream jarFile = new JarInputStream(new FileInputStream(jarName));
		JarEntry jarEntry;

		while(true)
		{
			jarEntry=jarFile.getNextJarEntry();
			if(jarEntry == null)
			{
				break;
			}
			if((jarEntry.getName().startsWith(packageName)) && (jarEntry.getName().endsWith(".class")) )
			{
				String classFileName = jarEntry.getName().replaceAll("/", "\\.");
				try
				{
					classes.add(Class.forName(classFileName.substring(0, classFileName.length() - 6)));
				}
				catch(ClassNotFoundException caughtException)
				{
					throw new IOException("class not found, do you have the right jar file?");
				}
			}
		}
		
		Class[] classesRet = new Class[classes.size()];
		classes.toArray(classesRet);
		return classesRet;
	}
}