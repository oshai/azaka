package oshai.azaka;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	private MyLocationListener locationListener = new MyLocationListener();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		Button button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				String message = "City: " + getCityName();
				Toast.makeText(MainActivity.this, message,
						Toast.LENGTH_SHORT).show();
			}

		});
		LocationManager locationManager = (LocationManager)
				getSystemService(Context.LOCATION_SERVICE);
		
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 5000, 10, locationListener);
		return true;
	}

	private String getCityName() {
		return locationListener.city();
	}

	private class MyLocationListener implements LocationListener {

	    private String city;

		@Override
	    public void onLocationChanged(Location loc) {
	        Toast.makeText(
	                getBaseContext(),
	                "Location changed: Lat: " + loc.getLatitude() + " Lng: "
	                    + loc.getLongitude(), Toast.LENGTH_SHORT).show();
	        String longitude = "Longitude: " + loc.getLongitude();
//	        Log.v(TAG, longitude);
	        String latitude = "Latitude: " + loc.getLatitude();
//	        Log.v(TAG, latitude);

	        /*------- To get city name from coordinates -------- */
	        String cityName = null;
	        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
	        List<Address> addresses;
	        try {
	            addresses = gcd.getFromLocation(loc.getLatitude(),
	                    loc.getLongitude(), 1);
	            if (addresses.size() > 0)
	                System.out.println(addresses.get(0).getLocality());
	            cityName = addresses.get(0).getLocality();
	        }
	        catch (IOException e) {
	            e.printStackTrace();
	        }
	        String s = longitude + "\n" + latitude + "\n\nMy Current City is: "
	            + cityName;
	        city = s;
	    }

	    public String city() {
			return city;
		}

		@Override
	    public void onProviderDisabled(String provider) {}

	    @Override
	    public void onProviderEnabled(String provider) {}

	    @Override
	    public void onStatusChanged(String provider, int status, Bundle extras) {}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}
}
