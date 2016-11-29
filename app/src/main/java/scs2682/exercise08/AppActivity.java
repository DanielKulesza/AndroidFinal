package scs2682.exercise08;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import scs2682.exercise08.data.Contact;
import scs2682.exercise08.ui.contacts.Contacts;
import scs2682.exercise08.ui.form.Form;

import static android.R.attr.bitmap;

public class AppActivity extends AppCompatActivity {

	private static final int SELECT_PICTURE = 1;

	private Bitmap bitmap;

	private Adapter  ad;

	private static final class Page {
		private final int layoutId;
		@NonNull
		private final String title;

		private Page(final int layoutId, @NonNull final String title) {
			this.layoutId = layoutId;
			this.title = title;
		}
	}

	public static final class Adapter extends PagerAdapter {
		private static final int POSITION_CONTACTS = 0;
		private static final int POSITION_FORM = 1;

		private final List<Page> pages;
		private final LayoutInflater layoutInflater;

		private final ViewPager viewPager;
		private Form form;

		public Contacts contacts;




		private Adapter(ViewPager viewPager) {
			Context context = viewPager.getContext();

			this.viewPager = viewPager;
			layoutInflater = LayoutInflater.from(context);

			pages = new ArrayList<>(2);
			pages.add(POSITION_CONTACTS, new Page(R.layout.contacts,
				context.getString(R.string.contacts)));
			pages.add(POSITION_FORM, new Page(R.layout.form,
				context.getString(R.string.update_contact)));


			//get contacts from shared preferences
		}

		@Override
		public Object instantiateItem(final ViewGroup container, final int position) {
			Page page = pages.get(position);
			View view = layoutInflater.inflate(page.layoutId, container, false);
			container.addView(view);

			if (view instanceof Contacts) {
				contacts = (Contacts) view;
				contacts.setAdapter(this);
			}
			else if (view instanceof Form) {
				form = (Form) view;
				form.setAdapter(this);
			}

			return view;
		}

		@Override
		public void destroyItem(final ViewGroup container, final int position,
			final Object object) {
			container.removeView((View) object);
		}

		@Override
		public CharSequence getPageTitle(final int position) {
			return pages.get(position).title;
		}

		@Override
		public int getCount() {
			return pages.size();
		}

		@Override
		public boolean isViewFromObject(final View view, final Object object) {
			return object == view;
		}

		public void onOpenForm(Contact contact, int positionInContacts) {
			viewPager.setCurrentItem(POSITION_FORM);

			form.updateContact(contact, positionInContacts);
		}

		public void onContactUpdated(Contact contact, int positionInContacts) {
			viewPager.setCurrentItem(POSITION_CONTACTS);
			contacts.updateContact(contact, positionInContacts);
		}
	}



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.appactivity);

		ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
		ad = new Adapter(viewPager);
		viewPager.setAdapter(ad);

	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Check which request we're responding to
		if (requestCode == SELECT_PICTURE) {
			// Make sure the request was successful
			if (resultCode == RESULT_OK) {
				// The user picked a contact.
				try {

					InputStream stream = getContentResolver().openInputStream(
							data.getData());
					bitmap = BitmapFactory.decodeStream(stream);
					stream.close();
					ImageView imageView = (ImageView) findViewById(R.id.photo);
					BitmapDrawable ob = new BitmapDrawable(getResources(), bitmap);
					imageView.setBackground(ob);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}