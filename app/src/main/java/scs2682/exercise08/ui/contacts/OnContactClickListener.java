package scs2682.exercise08.ui.contacts;

import android.support.annotation.NonNull;
import scs2682.exercise08.data.Contact;

public interface OnContactClickListener {
	void onContactClick(@NonNull Contact contact, int positionInContacts);
}
