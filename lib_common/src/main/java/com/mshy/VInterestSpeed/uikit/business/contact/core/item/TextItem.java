package com.mshy.VInterestSpeed.uikit.business.contact.core.item;

import android.text.TextUtils;

import com.mshy.VInterestSpeed.uikit.business.contact.core.model.ContactGroupStrategy;
import com.mshy.VInterestSpeed.uikit.business.contact.core.query.TextComparator;

public class TextItem extends AbsContactItem implements Comparable<TextItem> {
    private final String text;

    public TextItem(String text) {
        this.text = text != null ? text : "";
    }

    public final String getText() {
        return text;
    }

    @Override
    public int getItemType() {
        return ItemTypes.TEXT;
    }

    @Override
    public String belongsGroup() {
        String group = TextComparator.getLeadingUp(text);

        return !TextUtils.isEmpty(group) ? group : ContactGroupStrategy.GROUP_SHARP;
    }

    @Override
    public int compareTo(TextItem item) {
        return TextComparator.compareIgnoreCase(text, item.text);
    }
}
