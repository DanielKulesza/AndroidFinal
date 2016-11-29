package scs2682.exercise08.ui.contacts;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import scs2682.exercise08.AppActivity;
import scs2682.exercise08.AppActivity.Adapter;
import scs2682.exercise08.R;
import scs2682.exercise08.data.Contact;

import static android.content.Context.MODE_PRIVATE;

public class Contacts extends LinearLayout implements OnContactClickListener {
	private Adapter adapter;

	private final List<Contact> contacts = new ArrayList<>();

	private final ContactAdapter contactsAdapter;

	private  Context context;

	public Contacts(final Context context) {
		this(context, null, 0);

		this.context = context;

	}

	public Contacts(final Context context, final AttributeSet attrs) {
		this(context, attrs, 0);

		this.context = context;


	}

	public Contacts(final Context context, final AttributeSet attrs, final int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		contactsAdapter = new ContactAdapter(context, this);

		//Called when app is started up, appropriate time to load arraylist of all contacts into adapter

		SharedPreferences sharedPrefs = context.getSharedPreferences(AppActivity.class.getSimpleName(),MODE_PRIVATE);
		String data = sharedPrefs.getString("CONTACTS",null);
		Gson gson = new Gson();
		if(data==null){
			contactsAdapter.contacts = new ArrayList<>();
		}
		else{
			contactsAdapter.contacts = gson.fromJson(data, new TypeToken<List<Contact>>() {}.getType());
		}


		this.context = context;



	}




	public void updateContact(Contact contact, int positionInContacts) {
		// hide the keyboard
		InputMethodManager inputMethodManager
			= (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

		if (contact != null) {
			if (positionInContacts > -1) {
				// we are updating an already existing Contact in contacts
				contactsAdapter.contacts.set(positionInContacts, contact);
				contactsAdapter.notifyItemChanged(positionInContacts);



			}
			else {
				// -1 means add a new contact at the end of the list
				contactsAdapter.contacts.add(contact);
				contactsAdapter.notifyItemChanged(contactsAdapter.contacts.size() - 1);
			}
			//A bit hacked up, adds the whole array to shared preferences instead of each individual new contact. Will slow down machine at high number of contacts,
			//NEED TO FIX AFTER PROTOTYPING
			SharedPreferences sharedPrefs = context.getSharedPreferences(AppActivity.class.getSimpleName(),MODE_PRIVATE);
			SharedPreferences.Editor editor =  sharedPrefs.edit();
			Gson gson = new Gson();
			String json = gson.toJson(contactsAdapter.contacts);

			editor.putString("CONTACTS", json);
			editor.commit();
		}


	}

	@Override
	public void onContactClick(@NonNull final Contact contact, final int positionInContacts) {
		if (adapter != null) {
			adapter.onOpenForm(contact, positionInContacts);
		}
	}

	public void setAdapter(Adapter adapter) {
		this.adapter = adapter;
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		findViewById(R.id.add).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View view) {
				adapter.onOpenForm(null, -1);
			}
		});



		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		recyclerView.setAdapter(contactsAdapter);
	}
}