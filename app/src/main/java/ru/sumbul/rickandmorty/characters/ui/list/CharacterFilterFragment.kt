package ru.sumbul.rickandmorty.characters.ui.list

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.sumbul.rickandmorty.R
import ru.sumbul.rickandmorty.application.appComponent
import ru.sumbul.rickandmorty.databinding.FragmentCharacterFilterBinding
import ru.sumbul.rickandmorty.factory.CharactersViewModelFactory
import ru.sumbul.rickandmorty.util.StringArg
import javax.inject.Inject


class CharacterFilterFragment : Fragment() {

    companion object {
        var Bundle.nameA: String? by StringArg
        var Bundle.statusA: String? by StringArg
        var Bundle.typeA: String? by StringArg
        var Bundle.speciesA: String? by StringArg
        var Bundle.genderA: String? by StringArg
    }

    val charactersListFragment: CharactersListFragment = CharactersListFragment()

    @Inject
    lateinit var factory: CharactersViewModelFactory

    @OptIn(ExperimentalCoroutinesApi::class)
    val viewModel by viewModels<CharacterViewModel>(factoryProducer = { factory })

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
     //   showsDialog = true
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = ru.sumbul.rickandmorty.databinding.FragmentCharacterFilterBinding.inflate(
            inflater,
            container,
            false
        )

        var gender = ""
        binding.female.setOnClickListener {
            gender = "female"
        }
        binding.male.setOnClickListener {
            gender = "male"
        }
        binding.genderless.setOnClickListener {
            gender = "genderless"
        }
        binding.unknownGender.setOnClickListener {
            gender = "unknown"
        }


        var status = ""
        binding.alive.setOnClickListener {
            status = "alive"
        }
        binding.dead.setOnClickListener {
            status = "dead"
        }
        binding.unknown.setOnClickListener {
            status = "unknown"
        }



        binding.search.setOnClickListener {
            var name = binding.nameSearch.text.toString()
            name = if (name == "") {
                ""
            } else {
                "$name"
            }

            var species = ""
            species = if (binding.speciesSearch.text.toString() == "") {
                ""
            } else {
                binding.speciesSearch.text.toString()
            }

            var type = ""
            type = if (binding.typeSearch.text.toString() == "") {
                ""
            } else {
                binding.typeSearch.text.toString()
            }

            val bundle = Bundle()
            bundle.apply {
                putString("name", name)
                putString("status", status)
                putString("species", species)
                putString("gender", gender)
                putString("type", type)
            }
//
//            Bundle().apply {
//                nameA = name
//                statusA = status
//                typeA = type
//                speciesA = species
//                genderA = gender
//            }

        //    viewModel.filterCharactersOffline(name, status, species, type, gender)
            parentFragmentManager.setFragmentResult("filter", bundle)
            parentFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.frame_layout, charactersListFragment)
                .addToBackStack("filter")
                .commit()
        //    dialog?.dismiss()
        }

        return binding.root
    }

}


