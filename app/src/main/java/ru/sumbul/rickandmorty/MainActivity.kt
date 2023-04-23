package ru.sumbul.rickandmorty

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.sumbul.rickandmorty.characterDetails.CharacterDetailsFragment
import ru.sumbul.rickandmorty.characters.entity.Character
import ru.sumbul.rickandmorty.characters.ui.CharactersListFragment
import ru.sumbul.rickandmorty.databinding.ActivityMainBinding
import ru.sumbul.rickandmorty.episodes.ui.EpisodesListFragment
import ru.sumbul.rickandmorty.locationDetails.LocationDetailsFragment
import ru.sumbul.rickandmorty.locations.entity.Location
import ru.sumbul.rickandmorty.locations.ui.LocationsListFragment

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen() //должна быть первая иначе все сломается
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        replaceFragment(CharactersListFragment())

        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.characters -> replaceFragment(CharactersListFragment())
                R.id.episodes -> replaceFragment(EpisodesListFragment())
                R.id.locations -> replaceFragment(LocationsListFragment())

                else -> {
                }
            }
            true

        }

    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun onCharacterSelected(character: Character?) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        var containerViewId: Int = R.id.frame_layout
//        if (findViewById<View?>(R.id.framelayout_right) != null) containerViewId =
//            R.id.framelayout_right
        val bundle = Bundle()
        bundle.putSerializable("requestKey", character?.id)
        val characterDetailsFragment = CharacterDetailsFragment()
        characterDetailsFragment.arguments = bundle
        fragmentTransaction.replace(containerViewId, characterDetailsFragment)
        if (findViewById<View?>(R.id.frame_layout) == null) fragmentTransaction.addToBackStack(
            "null"
        )
        fragmentTransaction.commit()
    }

//    @OptIn(ExperimentalCoroutinesApi::class)
//    fun onLocationSelected(location: Location) {
//        val fragmentManager = supportFragmentManager
//        val fragmentTransaction = fragmentManager.beginTransaction()
//        var containerViewId: Int = R.id.frame_layout
////        if (findViewById<View?>(R.id.framelayout_right) != null) containerViewId =
////            R.id.framelayout_right
//        val bundle = Bundle()
//        bundle.putSerializable("requestKey4", location.id)
//        val locationDetailsFragment = LocationDetailsFragment()
//        locationDetailsFragment.arguments = bundle
//        fragmentTransaction.replace(containerViewId, locationDetailsFragment)
//        if (findViewById<View?>(R.id.frame_layout) == null) fragmentTransaction.addToBackStack(
//            "null"
//        )
//        fragmentTransaction.commit()
//    }


}