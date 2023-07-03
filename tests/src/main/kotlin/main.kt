import java.util.Scanner

val scan = Scanner(System.`in`)

class Edge(var next: Vertex, var weight: Int){
    fun change_weight(new_weight: Int){
        this.weight = new_weight
    }
}

class Vertex(var name : Char){
    var edges = mutableListOf<Edge>()
    var d: Int = Int.MAX_VALUE
    var used: Boolean = false
    var came_fron : Char? = null

    fun add_edge(next : Vertex, weight: Int){
        this.edges.add(Edge(next, weight))
    }

    fun delete_edge(ver: Vertex){
        for (elem in edges)
            if (elem.next == ver) {
                edges.remove(elem)
                break
            }
    }

}

class Graph{
    var vertices = mutableMapOf<Char, Vertex>()
    var start_vertex: Vertex? = null
    var destination_vertex: Vertex? = null


    fun add_vertex(name: Char){
        this.vertices.put(name, Vertex(name))
    }

    fun delete_vertex(name: Char){
        for (elem in vertices.values)
            elem.delete_edge(vertices[name]!!)
        vertices.remove(name)
    }

    fun delete_edge(name_from : Char, name_to : Char){
        vertices[name_from]!!.delete_edge(vertices[name_to]!!)
    }

    fun add_edge(name_from : Char, name_to : Char, weight: Int){
        vertices[name_from]!!.add_edge(vertices[name_to]!!, weight)
    }

    fun add_symmetric_edge(name_from : Char, name_to : Char, weight: Int){
        vertices[name_from]!!.add_edge(vertices[name_to]!!, weight)
        vertices[name_to]!!.add_edge(vertices[name_from]!!, weight)
    }

    fun set_start(name : Char){
        this.start_vertex = vertices[name]
    }

    fun set_destination(name : Char){
        this.destination_vertex = vertices[name]
    }

    fun rename_vertex(prev : Char, new : Char){
        val buff = vertices[prev] ?: return
        vertices.remove(prev)
        vertices.put(new, buff)
        vertices[new]!!.name = new
    }

    fun dijkstra(){
        start_vertex!!.d = 0
        for(i in vertices.values){
            var v : Vertex? = null
            for (j in vertices.values){
                if(!j.used && (v == null || j.d < v.d))
                    v = j
            }
            if (v!!.d == Int.MAX_VALUE)
                break
            v.used = true
            for (e in v.edges){
                if (v.d + e.weight < e.next.d) {
                    e.next.d = v.d + e.weight
                    e.next.came_fron = v.name
                }
            }
        }
    }

    fun print_res(){
        if (destination_vertex!!.d == Int.MAX_VALUE){
            print("oo")
            return
        }
        var res = mutableListOf<Char>()
        var elem = destination_vertex
        while (elem!!.name != start_vertex!!.name){
            res.add(elem!!.name)
            elem = vertices[elem.came_fron]
        }
        res.add(start_vertex!!.name)
        print("${res.reversed()} - ${destination_vertex!!.d}")
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
    gr.set_destination('5')

    gr.dijkstra()
    gr.print_res()
}