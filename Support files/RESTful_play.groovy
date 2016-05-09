import groovy.json.*
import groovy.swing.SwingBuilder

import java.awt.GridLayout
import javax.swing.ImageIcon
import javax.swing.WindowConstants as WC

//String key = new File('flickr_key.txt').text
def flkr_key = 82b5ec616a49c155abec5a5049a51cf1
String endPoint = 'https://api.flickr.com/services/rest?'
def params = [method: 'flickr.photos.search',
              api_key: flkr_key,
              format: 'json',
              tags: 'kitty',
              nojsoncallback: 1,
              media: 'photos',
              per_page: 6]

// Build URL and download JSON data
def qs = params.collect { it }.join('&')
String jsonTxt = "$endPoint$qs".toURL().text

// write formatted JSON data to file
File f = new File('cats.json')
if (f) f.delete()
f << JsonOutput.prettyPrint(jsonTxt)
println JsonOutput.prettyPrint(jsonTxt)

// parse JSON data and build URL for pictures
def json = new JsonSlurper().parseText(jsonTxt)
def photos = json.photos.photo

// build UI using Swing
new SwingBuilder().edt {
    frame(title:'Cat pictures', visible: true, pack: true,
        defaultCloseOperation: WC.EXIT_ON_CLOSE,
        layout:new GridLayout(0, 2, 2, 2)) {
        photos.each { p ->
            String url = "http://farm${p.farm}.staticflickr.com/${p.server}/${p.id}_${p.secret}.jpg"
            String title = p.title
            label(icon: new ImageIcon(url.toURL()), toolTipText: title)
        }
    }
}
