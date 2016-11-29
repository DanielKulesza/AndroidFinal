package scs2682.exercise08.ui.form;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import scs2682.exercise08.AppActivity.Adapter;
import scs2682.exercise08.AppActivity;
import scs2682.exercise08.R;
import scs2682.exercise08.data.Contact;
import android.app.Activity;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;

import static android.R.attr.data;
import static android.R.attr.drawable;
import static android.app.Activity.RESULT_OK;


public class Form extends LinearLayout {

	private ImageView photo;
	private TextView firstName;
	private TextView lastName;
	private TextView phone;
	private TextView email;

	private Adapter adapter;

    private static final int SELECT_PICTURE = 1;

	private int positionInContacts = -1;

	private  Context context;

	public Form(final Context context) {

		this(context, null, 0);

		this.context = context;
	}

	public Form(final Context context, final AttributeSet attrs) {

		this(context, attrs, 0);
		this.context = context;
	}

	public Form(final Context context, final AttributeSet attrs, final int defStyleAttr) {

		super(context, attrs, defStyleAttr);

		this.context = context;
	}

	public void updateContact(Contact contact, int positionInContacts) {
		this.positionInContacts = positionInContacts;

		if (contact != null) {
			// updating a contact
			Bitmap bmp = BitmapFactory.decodeByteArray(contact.photo, 0, contact.photo.length);
			Drawable drawable = new BitmapDrawable(bmp);
			photo.setBackground(drawable);
			firstName.setText(contact.firstName);
			lastName.setText(contact.lastName);
			phone.setText(contact.phoneNumber);
			email.setText(contact.email);
		}

		firstName.requestFocus();

		// open the keyboard
		InputMethodManager inputMethodManager
			= (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.showSoftInput(firstName, 0);
	}

	public void setAdapter(Adapter adapter) {
		this.adapter = adapter;
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		photo = (ImageView) findViewById(R.id.photo);
		firstName = (TextView) findViewById(R.id.firstName);
		lastName = (TextView) findViewById(R.id.lastName);
		phone = (TextView) findViewById(R.id.phone);
		email = (TextView) findViewById(R.id.email);






		findViewById(R.id.cancel).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				adapter.onContactUpdated(null, -1);
				photo.setBackgroundResource(R.mipmap.empty);
				firstName.setText("");
				lastName.setText("");
				phone.setText("");
				email.setText("");
			}
		});


        findViewById(R.id.photo)
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View arg0) {

                        // in onCreate or any event where your want the user to
                        // select a file
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
						((Activity) context).startActivityForResult(Intent.createChooser(intent,
                                "Select Picture"), SELECT_PICTURE);

                    }
                });


		findViewById(R.id.update).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {

				//encode image into byte array for storage in object
				Bitmap bmp = ((BitmapDrawable)photo.getBackground()).getBitmap();
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
				byte[] photoValue = stream.toByteArray();

				String firstNameValue = firstName.getText().toString();
				String lastNameValue = lastName.getText().toString();
				String phoneNumberValue = phone.getText().toString();
				String emailValue = email.getText().toString();

				if (TextUtils.isEmpty(firstNameValue) || TextUtils.isEmpty(lastNameValue)) {
					// first or last are empty - show toast
					Toast.makeText(getContext(),
						getContext().getString(R.string.error_first_and_last_name), Toast.LENGTH_SHORT)
						.show();
				}
				else {
					// bot first and last are valid, call adapter
					Contact contact = new Contact(photoValue, firstNameValue, lastNameValue, phoneNumberValue, emailValue);
					adapter.onContactUpdated(contact, positionInContacts);


					photo.setBackgroundResource(R.mipmap.empty);
					firstName.setText("");
					lastName.setText("");
					phone.setText("");
					email.setText("");
				}
			}
		});
	}
}
