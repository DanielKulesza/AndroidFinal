package scs2682.exercise08.data;

import android.support.annotation.NonNull;
public final class Contact {

	@NonNull
	public final byte[] photo;

	@NonNull
	public final String firstName;

	@NonNull
	public final String lastName;

	@NonNull
	public final String phoneNumber;

	@NonNull
	public final String email;


	public Contact(@NonNull final byte[] photo, @NonNull final String firstName, @NonNull final String lastName, @NonNull final String phoneNumber, @NonNull final String email) {
		this.photo = photo;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.email = email;
	}
}
