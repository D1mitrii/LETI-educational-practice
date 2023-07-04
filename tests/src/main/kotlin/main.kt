import java.util.Scanner

val scan = Scanner(System.`in`)

class Edge(var weight: Int){
    fun change_weight(new_weight: Int){
        this.weight = new_weight
    }
}

class Vertex(){
    var edges = mutableMapOf<Char, Edge>()
    var d: Int = Int.MAX_VALUE
    var used: Boolean = false

    fun add_edge(next : Char, weight: Int){
        this.edges.put(next, Edge(weight))
    }

    fun delete_edge(ver: Char){
        for (elem in edges.keys)
            if (elem == ver) {
                edges.remove(elem)
                break
            }
    }

}

class Graph {
    var vertices = mutableMapOf<Char, Vertex>()
    var start_vertex: Char? = null


    fun add_vertex(name: Char) {
        this.vertices.put(name, Vertex())
    }

    fun delete_vertex(name: Char) {
        for (elem in vertices.values)
            elem.delete_edge(name)
        vertices.remove(name)
    }

    fun delete_edge(name_from: Char, name_to: Char) {
        vertices[name_from]!!.delete_edge(name_to)
    }

    fun add_edge(name_from: Char, name_to: Char, weight: Int) {
        vertices[name_from]!!.add_edge(name_to, weight)
    }

    fun add_symmetric_edge(name_from: Char, name_to: Char, weight: Int) {
        vertices[name_from]!!.add_edge(name_to, weight)
        vertices[name_to]!!.add_edge(name_from, weight)
    }

    fun set_start(name: Char) {
        this.start_vertex = name
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
            for (e in v.edges.keys) {
                if (v.d + v.edges[e]!!.weight < vertices[e]!!.d) {
                    vertices[e]!!.d = v.d + v.edges[e]!!.weight
                }
            }
        }
    }

    fun edge_change_weight(from : Char, to : Char, new_weight: Int){
        vertices[from]!!.edges[to]?.change_weight(new_weight)
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

    gr.dijkstra()
    gr.print_res()
}