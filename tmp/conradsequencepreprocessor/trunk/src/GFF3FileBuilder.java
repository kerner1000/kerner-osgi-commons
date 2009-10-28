import java.util.ArrayList;
import java.util.List;

import de.bioutils.gff3.element.GFF3Element;
import de.bioutils.gff3.file.GFF3File;
import de.bioutils.gff3.file.GFF3FileImpl;


public class GFF3FileBuilder {

	private final List<GFF3Element> elements = new ArrayList<GFF3Element>();
	
	public void addElement(GFF3Element element) {
		elements.add(element);
	}

	public GFF3File build() {
		return new GFF3FileImpl(elements);
	}

}
