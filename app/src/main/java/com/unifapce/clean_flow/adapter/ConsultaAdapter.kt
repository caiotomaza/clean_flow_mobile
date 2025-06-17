package com.unifapce.clean_flow.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.unifapce.clean_flow.R
import com.unifapce.clean_flow.data.ConsultaArmazenamento
import com.unifapce.clean_flow.data.ConsultaEntrada
import com.unifapce.clean_flow.data.ConsultaSaida

class ConsultaAdapter(private var items: List<Any> = emptyList()) : RecyclerView.Adapter<ConsultaAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.iv_icon)
        val principal: TextView = view.findViewById(R.id.tv_principal)
        val secundario: TextView = view.findViewById(R.id.tv_secundario)
        val data: TextView = view.findViewById(R.id.tv_data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_consulta_residuo, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (val item = items[position]) {
            is ConsultaEntrada -> {
                holder.icon.setImageDrawable(ContextCompat.getDrawable(holder.itemView.context, R.drawable.ic_entrada))
                holder.principal.text = "${item.materialNome} (${item.pesoInicial})"
                holder.secundario.text = "Veículo: ${item.placaVeiculo}"
                holder.data.text = item.dataHora
            }
            is ConsultaSaida -> {
                holder.icon.setImageDrawable(ContextCompat.getDrawable(holder.itemView.context, R.drawable.ic_saida))
                holder.principal.text = "Saída do ${item.containerOrigem}"
                holder.secundario.text = "Veículo: ${item.placaVeiculo}"
                holder.data.text = item.dataHora
            }
            is ConsultaArmazenamento -> {
                holder.icon.setImageDrawable(ContextCompat.getDrawable(holder.itemView.context, R.drawable.ic_armazenamento))
                holder.principal.text = "${item.materialNome} em ${item.containerNome}"
                holder.secundario.text = "Peso atual: ${item.pesoAtual}"
                holder.data.text = "Última mov.: ${item.dataUltimaMovimentacao}"
            }
        }
    }

    override fun getItemCount() = items.size

    // Função para atualizar os dados da lista de forma eficiente
    fun updateData(newItems: List<Any>) {
        items = newItems
        notifyDataSetChanged() // Para este caso simples, é suficiente
    }
}
