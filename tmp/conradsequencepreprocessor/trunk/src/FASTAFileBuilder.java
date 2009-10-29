import java.util.ArrayList;
import java.util.List;

import de.bioutils.fasta.FASTAElement;
import de.bioutils.fasta.FASTAElementImpl;
import de.bioutils.fasta.NewFASTAFile;
import de.bioutils.fasta.NewFASTAFileImpl;

public class FASTAFileBuilder {

	private final List<FASTAElement> elements = new ArrayList<FASTAElement>();
	private int lineLength = FASTAElementImpl.DEFAULT_LINE_LENGTH;

	public synchronized void addElement(FASTAElement fastaElement) {
		fastaElement.setLineLength(lineLength);
		elements.add(fastaElement);
	}

	public synchronized NewFASTAFile build() {
		return new NewFASTAFileImpl(elements);
	}

	public synchronized void setLineLength(int lineLength) {
		// set length for all elements that have been already added
		if (elements.isEmpty()) {
			// nothing
		} else {
			for (FASTAElement e : elements) {
				e.setLineLength(lineLength);
			}
		}
		this.lineLength = lineLength;
	}

	public boolean containsElement(FASTAElement fastaEl) {
		if (elements.isEmpty())
			return false;
		for (FASTAElement e : elements) {
			if (e.equals(fastaEl))
				return true;
		}
		return false;
	}

}
