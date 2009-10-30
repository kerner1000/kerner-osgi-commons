import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.bioutils.gff3.element.GFF3Element;
import de.bioutils.gff3.file.GFF3File;
import de.bioutils.gff3.file.GFF3FileImpl;

public class GFF3FileBuilder {

	private final Collection<GFF3Element> elements = new ArrayList<GFF3Element>();
	private boolean sorted = false;

	GFF3FileBuilder() {
		
	}

	GFF3FileBuilder(GFF3File template) {
		this.elements.addAll(template.getElements());
		this.sorted = template.isSorted();
	}

	public void addElement(GFF3Element element) {
		elements.add(element);
	}

	public synchronized void setSorted(boolean sorted) {
		this.sorted = sorted;
	}

	public GFF3File build() {
		return new GFF3FileImpl(elements, sorted);
	}

	public void addAllElements(Collection<? extends GFF3Element> value) {
		elements.addAll(value);
	}

}
