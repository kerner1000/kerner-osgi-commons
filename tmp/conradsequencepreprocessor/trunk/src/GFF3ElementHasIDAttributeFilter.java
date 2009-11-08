import de.bioutils.gff3.attribute.AttributeGroup;
import de.bioutils.gff3.element.GFF3Element;
import de.bioutils.gff3.element.filter.GFF3ElementFilter;


public class GFF3ElementHasIDAttributeFilter implements GFF3ElementFilter {

	public boolean accept(GFF3Element e) {
		final AttributeGroup g = e.getAttributes();
		return g.hasKey("ID");
	}

}
