package com.code44.finance.api;

import android.content.Context;
import android.text.TextUtils;

import com.code44.finance.App;
import com.code44.finance.data.db.DBHelper;
import com.code44.finance.utils.EventBus;
import com.code44.finance.utils.Prefs;
import com.squareup.otto.Produce;

public class User extends Prefs {
    private static final String PREFIX = "user_";

    private static User singleton;

    private final DBHelper dbHelper;
    private final GcmRegistration gcmRegistration;
    private final EventBus eventBus;

    private String id;
    private String googleId;
    private String email;
    private String firstName;
    private String lastName;
    private String photoUrl;
    private String coverUrl;
    private boolean isPremium;

    private long currenciesTimestamp;
    private long categoriesTimestamp;
    private long accountsTimestamp;
    private long transactionsTimestamp;

    public User(Context context, DBHelper dbHelper, GcmRegistration gcmRegistration, EventBus eventBus) {
        super(context);
        this.dbHelper = dbHelper;
        this.gcmRegistration = gcmRegistration;
        this.eventBus = eventBus;

        refresh();
        eventBus.register(this);
    }

    public static synchronized User get() {
        if (singleton == null) {
            final Context context = App.getContext();
            final DBHelper dbHelper = DBHelper.get();
            final GcmRegistration gcmRegistration = GcmRegistration.get();
            final EventBus eventBus = EventBus.get();
            singleton = new User(context, dbHelper, gcmRegistration, eventBus);
        }
        return singleton;
    }

    @Override
    protected String getPrefix() {
        return PREFIX;
    }

    public void refresh() {
        id = getString("id", null);
        googleId = getString("googleId", null);
        email = getString("email", null);
        firstName = getString("firstName", null);
        lastName = getString("lastName", null);
        photoUrl = getString("photoUrl", null);
        coverUrl = getString("coverUrl", null);
        isPremium = getBoolean("isPremium", false);
        currenciesTimestamp = getLong("currenciesTimestamp", 0);
        categoriesTimestamp = getLong("categoriesTimestamp", 0);
        accountsTimestamp = getLong("accountsTimestamp", 0);
        transactionsTimestamp = getLong("transactionsTimestamp", 0);
    }

    public void clear() {
        clear("id", "googleId", "email", "firstName", "lastName", "photoUrl", "coverUrl", "isPremium", "currenciesTimestamp", "categoriesTimestamp", "accountsTimestamp", "transactionsTimestamp");
        refresh();
    }

    public void logout() {
        clear();
        gcmRegistration.clear();
        dbHelper.clear();
        notifyChanged();
    }

    public boolean isLoggedIn() {
        return !TextUtils.isEmpty(email);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        setString("id", id);
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
        setString("googleId", googleId);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        setString("email", email);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        setString("firstName", firstName);
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        setString("lastName", lastName);
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
        setString("photoUrl", photoUrl);
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
        setString("coverUrl", coverUrl);
    }

    public boolean isPremium() {
        return isPremium;
    }

    public void setPremium(boolean isPremium) {
        this.isPremium = isPremium;
        setBoolean("isPremium", isPremium);
    }

    public long getCurrenciesTimestamp() {
        return currenciesTimestamp;
    }

    public void setCurrenciesTimestamp(long currenciesTimestamp) {
        this.currenciesTimestamp = currenciesTimestamp;
        setLong("currenciesTimestamp", currenciesTimestamp);
    }

    public long getCategoriesTimestamp() {
        return categoriesTimestamp;
    }

    public void setCategoriesTimestamp(long categoriesTimestamp) {
        this.categoriesTimestamp = categoriesTimestamp;
        setLong("categoriesTimestamp", categoriesTimestamp);
    }

    public long getAccountsTimestamp() {
        return accountsTimestamp;
    }

    public void setAccountsTimestamp(long accountsTimestamp) {
        this.accountsTimestamp = accountsTimestamp;
        setLong("accountsTimestamp", accountsTimestamp);
    }

    public long getTransactionsTimestamp() {
        return transactionsTimestamp;
    }

    public void setTransactionsTimestamp(long transactionsTimestamp) {
        this.transactionsTimestamp = transactionsTimestamp;
    }

    public void notifyChanged() {
        eventBus.post(this);
    }

    @Produce public User produceUser() {
        return this;
    }
}
