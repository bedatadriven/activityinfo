package org.sigmah.shared.report.model.typeadapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.apache.commons.lang.NotImplementedException;
import org.sigmah.shared.report.model.labeling.ArabicNumberSequence;
import org.sigmah.shared.report.model.labeling.LabelSequence;
import org.sigmah.shared.report.model.labeling.LatinAlphaSequence;

public class NumberingAdapter extends XmlAdapter<NumberingType, LabelSequence> {

	@Override
	public NumberingType marshal(LabelSequence v) throws Exception {
		throw new NotImplementedException("NumberingType is deprecated");
	}

	@Override
	public LabelSequence unmarshal(NumberingType v) throws Exception {
		switch (v) {
			case ArabicNumerals:
				return new ArabicNumberSequence();
			case LatinAlphabet:
				return new LatinAlphaSequence();
		}

		return null;
	}

}
