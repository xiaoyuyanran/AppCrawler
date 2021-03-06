package com.testerhome.appcrawler.ut

import java.io.File

import com.testerhome.appcrawler.{CommonLog, _}
import com.testerhome.appcrawler.driver.AppiumClient
import com.testerhome.appcrawler.plugin.{DemoPlugin, Plugin}
import org.scalatest.FunSuite

import scala.reflect.internal.util.ScalaClassLoader.URLClassLoader
import scala.tools.nsc.{Global, Settings}

/**
  * Created by seveniruby on 16/8/10.
  */
class TestRuntimes extends FunSuite with CommonLog{

  val fileName="/Users/seveniruby/projects/LBSRefresh/iOS_20160813203343/AppCrawler_8.scala"
  test("MiniAppium dsl"){
//    AppiumClient.dsl("hello(\"seveniruby\", 30000)")
//    AppiumClient.dsl("hello(\"ruby\", 30000)")
//    AppiumClient.dsl(" hello(\"seveniruby\", 30000)")
//    AppiumClient.dsl("hello(\"seveniruby\", 30000 )  ")
//    AppiumClient.dsl("sleep(3)")
//    AppiumClient.dsl("hello(\"xxxxx\")")
//    AppiumClient.dsl("println(com.testerhome.appcrawler.AppCrawler.crawler.driver)")

  }

  test("compile by scala"){
    Runtimes.init(new File(fileName).getParent)
    Runtimes.compile(List(fileName))



  }

  test("native compile"){


    val outputDir=new File(fileName).getParent

    val settings = new Settings()
    settings.deprecation.value = true // enable detailed deprecation warnings
    settings.unchecked.value = true // enable detailed unchecked warnings
    settings.outputDirs.setSingleOutput(outputDir)
    settings.usejavacp.value = true

    val global = new Global(settings)
    val run = new global.Run
    run.compile(List(fileName))

  }


  test("imain"){

    Runtimes.init()
    Runtimes.eval(
      """
        |import com.testerhome.appcrawler.MiniAppium
        |println("xxx")
        |println("ddd")
        |MiniAppium.hello("222")
      """.stripMargin)


  }


  test("imain q"){

    Runtimes.init()
    Runtimes.eval("import com.testerhome.appcrawler.MiniAppium")
    Runtimes.eval(
      """
        |println("xxx")
        |println("ddd")
        |MiniAppium.hello("222")
      """.stripMargin)


  }


  test("imain with MiniAppium"){

    Runtimes.init()
    Runtimes.eval("import com.testerhome.appcrawler.MiniAppium._")
    Runtimes.eval(
      """
        |hello("222")
        |println(driver)
      """.stripMargin)
  }

  test("compile plugin"){
    Runtimes.init()
    Runtimes.compile(List("src/universal/plugins/DynamicPlugin.scala"))
    val p=Class.forName("com.testerhome.appcrawler.plugin.DynamicPlugin").newInstance()
    log.info(p)


  }

  test("test classloader"){
    val classPath="target/tmp/"
    Runtimes.init(classPath)
    Runtimes.compile(List("/Users/seveniruby/projects/LBSRefresh/src/universal/plugins/"))
    val urls=Seq(new java.io.File(classPath).toURI.toURL)
    val loader=new URLClassLoader(urls, ClassLoader.getSystemClassLoader)
    val x=loader.loadClass("AppCrawler_5").newInstance().asInstanceOf[FunSuite]
    log.info(x.testNames)
    log.info(getClass.getCanonicalName)

    log.info(getClass.getProtectionDomain.getCodeSource.getLocation.getPath)

  }

  test("load plugins"){

    val a=new DemoPlugin()
    log.info(a.asInstanceOf[Plugin])
    //getClass.getClassLoader.asInstanceOf[URLClassLoader].loadClass("DynamicPlugin")
    val plugins=Runtimes.loadPlugins("/Users/seveniruby/projects/LBSRefresh/src/universal/plugins/")
    plugins.foreach(log.info)

  }


  test("crawl keyword"){
    Runtimes.eval("def crawl(depth:Int)=com.testerhome.appcrawler.AppCrawler.crawler.crawl(depth)")
    Runtimes.eval("crawl(1)")
  }


}

