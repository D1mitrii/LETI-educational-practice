import com.sun.org.apache.xpath.internal.operations.Bool
import java.util.Scanner

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import javafx.stage.StageStyle

val scan = Scanner(System.`in`)

data class Edge(var start: Vertex, var end: Vertex, var weight: Int)

class Vertex(){
    var edges = mutableListOf<Edge>()
    var d: Int = Int.MAX_VALUE
    var used: Boolean = false

    fun add_edge(next : Vertex, weight: Int){
        this.edges.add(Edge(this, next, weight))
    }

    fun delete_edge(ver: Vertex){
        for (elem in edges)
            if (elem.end == ver) {
                edges.remove(elem)
                break
            }
    }

    fun change_weight(end: Vertex?, new_weight: Int){
        for (elem in edges)
            if (elem.end == end) {
                elem.weight = new_weight
                break
            }
    }

}

class Graph {
    var vertices = mutableMapOf<Char, Vertex>()
    var start_vertex: Char? = null

    var current_vertex: Vertex? = null //current vertex
    var current_edge_index: Int = 0 //edge index to check for current vertex
    var checked_vertex: Int = 0 //number of checked vertices

    fun add_vertex(name: Char) {
        this.vertices.put(name, Vertex())
    }

    fun delete_vertex(name: Char) {
        for (elem in vertices.values)
            elem.delete_edge(vertices[name]!!)
        vertices.remove(name)
    }

    fun delete_edge(name_from: Char, name_to: Char) {
        vertices[name_from]!!.delete_edge(vertices[name_to]!!)
    }

    fun add_edge(name_from: Char, name_to: Char, weight: Int) {
        vertices[name_from]!!.add_edge(vertices[name_to]!!, weight)
    }

    fun add_symmetric_edge(name_from: Char, name_to: Char, weight: Int) {
        vertices[name_from]!!.add_edge(vertices[name_to]!!, weight)
        vertices[name_to]!!.add_edge(vertices[name_from]!!, weight)
    }

    fun set_start(name: Char) {
        this.start_vertex = name
        vertices[start_vertex]!!.d = 0
    }

    fun rename_vertex(prev: Char, new: Char) {
        val buff = vertices[prev] ?: return
        vertices.remove(prev)
        vertices.put(new, buff)
    }

    fun dijkstra() {
        vertices[start_vertex]!!.d = 0
        for (i in vertices.values) {
            var v: Vertex? = null
            for (j in vertices.values) {
                if (!j.used && (v == null || j.d < v.d))
                    v = j
            }
            if (v!!.d == Int.MAX_VALUE)
                break
            v.used = true
            for (e in v.edges) {
                if (v.d + e.weight < e.end.d)
                    e.end.d = v.d + e.weight
            }
        }
    }

    fun choose_new_vertex() { //choose next vertex to check
        var v: Vertex? = null
        for (j in vertices.values) {
            if (!j.used && (v == null || j.d < v.d))
                v = j
        }
        if (v!!.d == Int.MAX_VALUE) {
            checked_vertex++
            return
        }
        v.used = true
        current_vertex = v
        current_edge_index = 0
        checked_vertex++
    }

    fun update_edge()  { // every step check 1 edge
        while ((current_edge_index < current_vertex!!.edges.size) && (current_vertex!!.edges[current_edge_index].end.used == true)) //if next vertex is already used check next
            this.current_edge_index++
        if (current_edge_index < current_vertex!!.edges.size) {
            if (current_vertex!!.d + current_vertex!!.edges[current_edge_index].weight < current_vertex!!.edges[current_edge_index].end.d)
                current_vertex!!.edges[current_edge_index].end.d = current_vertex!!.d + current_vertex!!.edges[current_edge_index].weight
            this.current_edge_index++
        } else
            current_vertex = null
    }

    fun next_step(): Boolean{ //true - end of algorithm, false - there are unseen vertices
        if (checked_vertex == vertices.size - 1)
            return true
        if (current_vertex == null)
            choose_new_vertex()
        else
            update_edge()
        return false
    }

    fun edge_change_weight(from : Char, to : Char, new_weight: Int){
        vertices[from]!!.change_weight(vertices[to], new_weight)
    }

    fun print_res() {
        print("rasstoyaniya from vertex ${start_vertex}\n")
        for (elem in vertices.keys)
            print("$elem\t")
        print("\n")
        for (elem in vertices.values)
            print(when{
                elem.d == Int.MAX_VALUE -> "oo"
                else -> "${elem.d}\t"
            })
    }
}
fun main(){

    // здесь тест примера с википедии https://ru.wikipedia.org/wiki/Алгоритм_Дейкстры
    var gr = Graph()
    gr.add_vertex('1')
    gr.add_vertex('2')
    gr.add_vertex('3')
    gr.add_vertex('4')
    gr.add_vertex('5')
    gr.add_vertex('6')
    gr.add_vertex('7')
    gr.add_vertex('8')


    gr.add_symmetric_edge('1', '2', 7)
    gr.add_symmetric_edge('1', '3', 9)
    gr.add_symmetric_edge('1', '6', 14)
    gr.add_symmetric_edge('2', '3', 10)
    gr.add_symmetric_edge('2', '7', 1)
    gr.add_symmetric_edge('2', '4', 15)
    gr.add_symmetric_edge('3', '4', 11)
    gr.add_symmetric_edge('3', '6', 2)
    gr.add_symmetric_edge('4', '5', 6)
    gr.add_symmetric_edge('5', '6', 9)

    gr.add_symmetric_edge('1', '7', 1)
    gr.delete_vertex('7')

    gr.set_start('1')

    //gr.dijkstra()

    while(!gr.next_step()){}

    gr.print_res()
}