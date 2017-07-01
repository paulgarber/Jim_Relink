import java.io.File;
//import java.io.*;
import java.io.FileOutputStream;
//import java.util.zip.ZipEntry;
//import java.util.zip.ZipOutputStream;
import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;

import com.justinmind.prototyper.api.API;
import com.justinmind.prototyper.api.IPrototype;
import com.justinmind.prototyper.api.ui.canvas.ICanvas;
import com.justinmind.util.file.FileUtils;
	
public class Relink {
		/**
		 * @param args
		 */
		
		public static String replaceMatching2(String input, String lowerBound, String upperBound, String newPath){
		      String result = input.replaceAll("(.*?"+lowerBound + ")" + newPath + "(" + upperBound + ".*)", "$1$3");
		      return result;
		 }
		
		public static void main(String[] args) {
			// TODO Auto-generated method stub
			FileDialog dialog = new FileDialog(new Shell());
			String path_to_vp = dialog.open();
			try {
			String temp_directory = System.getProperty("java.io.tmpdir");
			//String temp_directory = "/users/pgarber/desktop/";
			FileUtils.unZip(path_to_vp, temp_directory + File.separator + "work");
			String the_path = (temp_directory + File.separator + "work");
			IPrototype prototype = API.getPrototypeLoader().loadPrototype(temp_directory + File.separator + "work");
			List<ICanvas> canvas = prototype.getApiCanvases();
			for (ICanvas current : canvas) {
			System.out.println(current.getApiName());
			}
			DirectoryDialog dialog_two = new DirectoryDialog(new Shell());
			String new_assets_unfixed = dialog_two.open();
			String new_assets = new_assets_unfixed.concat("/");
			
		    File f = new File(the_path + "/screens/"); // the path with all the screens

		    File[] files = f.listFiles();
		    for (File file : files) {
		        if (file.isDirectory()) {
		            System.out.print("directory:");
		        } else {
		            System.out.print("     file:");
		            String file_content = FileUtils.readFile(file.getCanonicalPath());
		            //String updated_content = file_content.replaceAll("(&<remote-path>=)[^&]*(&Assets/=)", new_assets);
		            //String updated_content = file_content.replaceAll("(.*?"+ "<remote-path>" + ")" + "" + "(" + "Assets/" + ".*)", "$1$3");
		            String updated_content = file_content.replaceAll("(?<=<remote-path>)(.*)(?=Assets)", new_assets);
		            //String updated_content = "derp";
		            System.out.print(updated_content);
		            FileOutputStream fop = null;
		            fop = new FileOutputStream(file);
		            byte[] contentInBytes = updated_content.getBytes();
		            fop.write(contentInBytes);
		            fop.flush();
		            fop.close();
		            FileUtils.deepCreateFile(file);
		             
		        }
		        System.out.println(file.getCanonicalPath());
		    }
			List<String> exclusions = Arrays.asList();
			String home = System.getProperty("user.home");
			String desktop = (home + File.separator + "Desktop" + File.separator + "output.vp");
			FileUtils.zipProject(the_path, desktop, exclusions);
			//FileUtils.deepDeleteDirExceptRoot(temp_directory + File.separator + "work");
			} catch(Exception ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
			}
		}

}

