package scs2682.exercise08.ui.contacts;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;

import scs2682.exercise08.R;
import scs2682.exercise08.data.Contact;

public class CellViewHolder extends RecyclerView.ViewHolder {
	private final ImageView photoCell;
	private final TextView firstName;
	private final TextView lastName;
	private final TextView phone;
	private final TextView email;

	private int positionInContacts = -1;

	public CellViewHolder(View view, final OnContactClickListener onContactClickListener) {
		super(view);

		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				if (onContactClickListener != null) {
					//convert photo to byte code
					Bitmap bmp = ((BitmapDrawable)photoCell.getBackground()).getBitmap();
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
					byte[] photoValue = stream.toByteArray();



					Contact contact = new Contact(photoValue, firstName.getText().toString(), lastName.getText().toString(), phone.getText().toString(), email.getText().toString());
					onContactClickListener.onContactClick(contact, positionInContacts);
				}
			}
		});

		photoCell = (ImageView) view.findViewById(R.id.photoCell);
		firstName = (TextView) view.findViewById(R.id.firstName);
		lastName = (TextView) view.findViewById(R.id.lastName);
		phone = (TextView) view.findViewById(R.id.phone);
		email = (TextView) view.findViewById(R.id.email);
	}

	public void update(Contact contact, int positionInContacts) {

		Bitmap bmp = BitmapFactory.decodeByteArray(contact.photo, 0, contact.photo.length);
		Drawable d = new BitmapDrawable(bmp);
		photoCell.setBackground(d);
		firstName.setText(contact.firstName);
		lastName.setText(contact.lastName);
		phone.setText(contact.phoneNumber);
		email.setText(contact.email);

		this.positionInContacts = positionInContacts;
	}
}
