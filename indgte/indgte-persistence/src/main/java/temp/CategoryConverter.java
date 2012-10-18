package temp;

import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;

public class CategoryConverter {
	public static void main(String[] args) {
		new CategoryConverter().fix();
	}
	
	public void fix() {
		//File categories = this.getClass().getClassLoader().getResource("temp"); 
		Path path = FileSystems.getDefault().getPath("/home/mbmartinez/git/indgte/indgte/indgte-persistence/src/main/java/temp/temp");
		try {
			DirectoryStream<Path> ds = Files.newDirectoryStream(path, "*.xml");
			for(Path p : ds) {
				Path newfile = FileSystems.getDefault().getPath("/home/mbmartinez/git/indgte/indgte/indgte-persistence/src/main/resources/categories/" + p.getFileName().toString().substring(0, p.getFileName().toString().length() - ".xml".length()) + ".csv");
				List<String> contents = Files.readAllLines(p, StandardCharsets.UTF_8);
				System.out.println("contents:" + contents);
				BufferedWriter writer = Files.newBufferedWriter(newfile, StandardCharsets.UTF_8);
				for(Iterator<String> i = contents.iterator(); i.hasNext();) {
					String s = i.next();
					if(s.contains("base")) {
						writer.write(s.trim().replaceAll("<base>", "").replaceAll("</base>",","));
					}
				}
				writer.flush();
				writer.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
