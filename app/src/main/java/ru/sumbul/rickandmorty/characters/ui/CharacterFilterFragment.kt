package ru.sumbul.rickandmorty.characters.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import ru.sumbul.rickandmorty.R
import ru.sumbul.rickandmorty.databinding.FragmentCharacterFilterBinding


class CharacterFilterFragment : Fragment() {

    val charactersListFragment: CharactersListFragment = CharactersListFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentCharacterFilterBinding.inflate(
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
        //   name=rick&status=alive


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
                putString("gender", gender)
            }
            parentFragmentManager.setFragmentResult("filter", bundle)
            parentFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.frame_layout, charactersListFragment)
                .addToBackStack("filter")
                .commit()

        }
//СДЕЛАТЬ ПАГИНАЦИЮ НА ЛИСТЕ С ПЕРСОНАЖАМИ ЛИБО НОВЫЙ ЛИСТ С ПЕРСОНАЖАМИ НА КОТОРОМ БУДЕТ РЕЗ-АТ ПОИСКА И НОВУЮ ПАГИНАЦИЮ
        // И ТАМ ЖЕ КНОПКА СБРОСИТЬ ФИЛЬТРЫ КОТОРАЯ ПРИВЕДЕТ НА ПЕРВОНАЧАЛЬНЫЙ ФРАГМЕНТ НО ТОГДА МНЕ КАЖЕТСЯ ТАК НЕЛЬЗЯЯЯ

        return binding.root
    }

//    private fun filterContacts(question: String): List<Contact> {
//        return dataset.filter { it.name.contains(question, ignoreCase = true) }
//    }

}


