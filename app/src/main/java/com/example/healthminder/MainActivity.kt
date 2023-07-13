package com.example.healthminder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.widget.TimePicker
import android.widget.Toast
import java.util.*

class MainActivity : AppCompatActivity() {

    private val lembretes: MutableList<Lembrete> = mutableListOf()
    private val limiteMaximo = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClickImageButton(view: View) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_custom, null)

        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Novo Lembrete")
            .setPositiveButton("Salvar") { dialog, which ->
                val descricaoEditText = dialogView.findViewById<EditText>(R.id.editTextDescricao)
                val horarioInicialEditText = dialogView.findViewById<EditText>(R.id.editTextHorarioInicial)
                val dosesIntervalosEditText = dialogView.findViewById<EditText>(R.id.editTextDosesIntervalos)
                val intervaloTempoEditText = dialogView.findViewById<EditText>(R.id.editTextIntervaloTempo)

                val descricao = descricaoEditText.text.toString()
                val horarioInicial = horarioInicialEditText.text.toString()
                val dosesIntervalos = dosesIntervalosEditText.text.toString()
                val intervaloTempo = intervaloTempoEditText.text.toString()

                if (validateFields(descricao, horarioInicial, dosesIntervalos, intervaloTempo)) {
                    salvarLembrete(descricao, horarioInicial, dosesIntervalos, intervaloTempo)
                    Toast.makeText(this, "Lembrete salvo com sucesso!!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Preencha todos os campos corretamente pfzin! :D", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)

        val dialog = dialogBuilder.create()
        dialog.show()
    }


    fun onClickTimePicker(view: View) {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            val editTextHorario = findViewById<EditText>(R.id.editTextHorarioInicial)
            val time = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute)
            editTextHorario.setText(time)
        }, currentHour, currentMinute, true)

        timePickerDialog.show()
    }

    private fun validateFields(descricao: String, horarioInicial: String, dosesIntervalos: String, intervaloTempo: String): Boolean {
        return descricao.isNotBlank() && dosesIntervalos.isNumeric() && horarioInicial.isTimeValid() && intervaloTempo.isTimeValid()
    }

    private fun salvarLembrete(descricao: String, horarioInicial: String, dosesIntervalos: String, intervaloTempo: String) {
        if (lembretes.size >= limiteMaximo) {
            lembretes.removeAt(0)
        }

        val lembrete = Lembrete(descricao, horarioInicial, dosesIntervalos, intervaloTempo)
        lembretes.add(lembrete)
    }

    fun exibirLembretes(view: View) {
        for (lembrete in lembretes) {
            println("Descrição: ${lembrete.descricao}")
            println("Horário Inicial: ${lembrete.horarioInicial}")
            println("Doses/Intervalos: ${lembrete.dosesIntervalos}")
            println("Intervalo de Tempo: ${lembrete.intervaloTempo}")
            println()
        }
    }

    data class Lembrete(
        val descricao: String,
        val horarioInicial: String,
        val dosesIntervalos: String,
        val intervaloTempo: String
    )

    // Limitalção para que o campo contenha apenas dígitos numéricos
    fun String.isNumeric(): Boolean {
        return this.matches("-?\\d+(\\.\\d+)?".toRegex())
    }

    // Validação se campo a ser enviado representa um horário válido no formato de hora e minuto(HH,mm)
    fun String.isTimeValid(): Boolean {
        if (length != 4)
            return false

        val hour = substring(0, 2).toIntOrNull()
        val minute = substring(2, 4).toIntOrNull()

        return hour in 0..23 && minute in 0..59
    }
}

object CalculadoraActivity {

}
