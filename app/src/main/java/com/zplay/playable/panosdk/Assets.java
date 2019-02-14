package com.zplay.playable.panosdk;

/**
 * Description:
 * <p>
 * Created by lgd on 2019/2/13.
 */
public class Assets {
    public static final String MRAID_JS = "(function() {\n" +
            "\n" +
            "  /***************************************************************************\n" +
            "   * console logging helper\n" +
            "   **************************************************************************/\n" +
            "\n" +
            "  var console = {};\n" +
            "  console.log = function(msg) {\n" +
            "    if (typeof enableLog != 'undefined') {\n" +
            "      var iframe = document.createElement(\"IFRAME\");\n" +
            "      iframe.setAttribute(\"src\", \"console-log://\" + msg);\n" +
            "      document.documentElement.appendChild(iframe);\n" +
            "      iframe.parentNode.removeChild(iframe);\n" +
            "      iframe = null;\n" +
            "    }\n" +
            "  };\n" +
            "\n" +
            "  LogLevelEnum = {\n" +
            "    \"DEBUG\"   : 0,\n" +
            "    \"INFO\"    : 1,\n" +
            "    \"WARNING\" : 2,\n" +
            "    \"ERROR\"   : 3\n" +
            "  }\n" +
            "\n" +
            "  var logLevel = LogLevelEnum.DEBUG;\n" +
            "  var log = {};\n" +
            "\n" +
            "  log.d = function(msg) {\n" +
            "    if (logLevel <= LogLevelEnum.DEBUG) {\n" +
            "      console.log(\"(D) \" + msg);\n" +
            "    }\n" +
            "  }\n" +
            "\n" +
            "  log.i = function(msg) {\n" +
            "    if (logLevel <= LogLevelEnum.INFO) {\n" +
            "      console.log(\"(I) \" + msg);\n" +
            "    }\n" +
            "  }\n" +
            "\n" +
            "  log.w = function(msg) {\n" +
            "    if (logLevel <= LogLevelEnum.WARN) {\n" +
            "      console.log(\"(W) \" + msg);\n" +
            "    }\n" +
            "  }\n" +
            "\n" +
            "  log.e = function(msg) {\n" +
            "    console.log(\"(E) \" + msg);\n" +
            "  }\n" +
            "\n" +
            "  /***************************************************************************\n" +
            "   * MRAID declaration\n" +
            "   **************************************************************************/\n" +
            "\n" +
            "  log.i(\"Setting up mraid object\");\n" +
            "\n" +
            "  var mraid = window.mraid = {};\n" +
            "\n" +
            "  /***************************************************************************\n" +
            "   * constants\n" +
            "   **************************************************************************/\n" +
            "\n" +
            "  var VERSION = \"2.0\";\n" +
            "\n" +
            "  var STATES = mraid.STATES = {\n" +
            "    \"LOADING\" : \"loading\",\n" +
            "    \"DEFAULT\" : \"default\",\n" +
            "    \"EXPANDED\" : \"expanded\",\n" +
            "    \"RESIZED\" : \"resized\",\n" +
            "    \"HIDDEN\" : \"hidden\"\n" +
            "  };\n" +
            "\n" +
            "  var PLACEMENT_TYPES = mraid.PLACEMENT_TYPES = {\n" +
            "    \"INLINE\" : \"inline\",\n" +
            "    \"INTERSTITIAL\" : \"interstitial\"\n" +
            "  };\n" +
            "\n" +
            "  var RESIZE_PROPERTIES_CUSTOM_CLOSE_POSITION = mraid.RESIZE_PROPERTIES_CUSTOM_CLOSE_POSITION = {\n" +
            "    \"TOP_LEFT\" : \"top-left\",\n" +
            "    \"TOP_CENTER\" : \"top-center\",\n" +
            "    \"TOP_RIGHT\" : \"top-right\",\n" +
            "    \"CENTER\" : \"center\",\n" +
            "    \"BOTTOM_LEFT\" : \"bottom-left\",\n" +
            "    \"BOTTOM_CENTER\" : \"bottom-center\",\n" +
            "    \"BOTTOM_RIGHT\" : \"bottom-right\"\n" +
            "  };\n" +
            "\n" +
            "  var ORIENTATION_PROPERTIES_FORCE_ORIENTATION = mraid.ORIENTATION_PROPERTIES_FORCE_ORIENTATION = {\n" +
            "    \"PORTRAIT\" : \"portrait\",\n" +
            "    \"LANDSCAPE\" : \"landscape\",\n" +
            "    \"NONE\" : \"none\"\n" +
            "  };\n" +
            "\n" +
            "  var EVENTS = mraid.EVENTS = {\n" +
            "    \"ERROR\" : \"error\",\n" +
            "    \"READY\" : \"ready\",\n" +
            "    \"SIZECHANGE\" : \"sizeChange\",\n" +
            "    \"STATECHANGE\" : \"stateChange\",\n" +
            "    \"VIEWABLECHANGE\" : \"viewableChange\"\n" +
            "  };\n" +
            "\n" +
            "  var SUPPORTED_FEATURES = mraid.SUPPORTED_FEATURES = {\n" +
            "    \"SMS\" : \"sms\",\n" +
            "    \"TEL\" : \"tel\",\n" +
            "    \"CALENDAR\" : \"calendar\",\n" +
            "    \"STOREPICTURE\" : \"storePicture\",\n" +
            "    \"INLINEVIDEO\" : \"inlineVideo\"\n" +
            "  };\n" +
            "\n" +
            "  /***************************************************************************\n" +
            "   * state\n" +
            "   **************************************************************************/\n" +
            "\n" +
            "  var state = STATES.LOADING;\n" +
            "  var placementType = PLACEMENT_TYPES.INLINE;\n" +
            "  var supportedFeatures = {};\n" +
            "  var isViewable = false;\n" +
            "  var isResizeReady = false;\n" +
            "\n" +
            "  var expandProperties = {\n" +
            "    \"width\" : 0,\n" +
            "    \"height\" : 0,\n" +
            "    \"useCustomClose\" : false,\n" +
            "    \"isModal\" : true\n" +
            "  };\n" +
            "\n" +
            "  var orientationProperties = {\n" +
            "    \"allowOrientationChange\" : true,\n" +
            "    \"forceOrientation\" : ORIENTATION_PROPERTIES_FORCE_ORIENTATION.NONE\n" +
            "  };\n" +
            "\n" +
            "  var resizeProperties = {\n" +
            "    \"width\" : 0,\n" +
            "    \"height\" : 0,\n" +
            "    \"customClosePosition\" : RESIZE_PROPERTIES_CUSTOM_CLOSE_POSITION.TOP_RIGHT,\n" +
            "    \"offsetX\" : 0,\n" +
            "    \"offsetY\" : 0,\n" +
            "    \"allowOffscreen\" : true\n" +
            "  };\n" +
            "\n" +
            "  var currentPosition = {\n" +
            "    \"x\" : 0,\n" +
            "    \"y\" : 0,\n" +
            "    \"width\" : 0,\n" +
            "    \"height\" : 0\n" +
            "  };\n" +
            "\n" +
            "  var defaultPosition = {\n" +
            "    \"x\" : 0,\n" +
            "    \"y\" : 0,\n" +
            "    \"width\" : 0,\n" +
            "    \"height\" : 0\n" +
            "  };\n" +
            "\n" +
            "  var maxSize = {\n" +
            "    \"width\" : 0,\n" +
            "    \"height\" : 0\n" +
            "  };\n" +
            "\n" +
            "  var screenSize = {\n" +
            "    \"width\" : 0,\n" +
            "    \"height\" : 0\n" +
            "  };\n" +
            "\n" +
            "  var currentOrientation = 0;\n" +
            "\n" +
            "  var listeners = {};\n" +
            "\n" +
            "  /***************************************************************************\n" +
            "   * \"official\" API: methods called by creative\n" +
            "   **************************************************************************/\n" +
            "\n" +
            "  mraid.addEventListener = function(event, listener) {\n" +
            "    log.i(\"mraid.addEventListener \" + event + \": \" + String(listener));\n" +
            "    if (!event || !listener) {\n" +
            "      mraid.fireErrorEvent(\"Both event and listener are required.\", \"addEventListener\");\n" +
            "      return;\n" +
            "    }\n" +
            "    if (!contains(event, EVENTS)) {\n" +
            "      mraid.fireErrorEvent(\"Unknown MRAID event: \" + event, \"addEventListener\");\n" +
            "      return;\n" +
            "    }\n" +
            "    var listenersForEvent = listeners[event] = listeners[event] || [];\n" +
            "    // check to make sure that the listener isn't already registered\n" +
            "    for (var i = 0; i < listenersForEvent.length; i++) {\n" +
            "      var str1 = String(listener);\n" +
            "      var str2 = String(listenersForEvent[i]);\n" +
            "      if (listener === listenersForEvent[i] || str1 === str2) {\n" +
            "        log.i(\"listener \" + str1 + \" is already registered for event \" + event);\n" +
            "        return;\n" +
            "      }\n" +
            "    }\n" +
            "    listenersForEvent.push(listener);\n" +
            "  };\n" +
            "\n" +
            "  mraid.createCalendarEvent = function(parameters) {\n" +
            "    log.i(\"mraid.createCalendarEvent with \" + parameters);\n" +
            "    callNative(\"createCalendarEvent?eventJSON=\"+JSON.stringify(parameters));\n" +
            "  };\n" +
            "\n" +
            "  mraid.close = function() {\n" +
            "    log.i(\"mraid.close\");\n" +
            "    if (state === STATES.LOADING ||\n" +
            "      (state === STATES.DEFAULT && placementType === PLACEMENT_TYPES.INLINE) ||\n" +
            "      state === STATES.HIDDEN) {\n" +
            "      // do nothing\n" +
            "      return;\n" +
            "    }\n" +
            "    callNative(\"close\");\n" +
            "  };\n" +
            "\n" +
            "  mraid.expand = function(url) {\n" +
            "    if (url === undefined) {\n" +
            "      log.i(\"mraid.expand (1-part)\");\n" +
            "    } else {\n" +
            "      log.i(\"mraid.expand \" + url);\n" +
            "    }\n" +
            "    // The only time it is valid to call expand is when the ad is\n" +
            "    // a banner currently in either default or resized state.\n" +
            "    if (placementType !== PLACEMENT_TYPES.INLINE ||\n" +
            "      (state !== STATES.DEFAULT && state !== STATES.RESIZED)) {\n" +
            "      return;\n" +
            "    }\n" +
            "    if (url === undefined) {\n" +
            "      callNative(\"expand\");\n" +
            "    } else {\n" +
            "      callNative(\"expand?url=\" + encodeURIComponent(url));\n" +
            "    }\n" +
            "  };\n" +
            "\n" +
            "  mraid.getCurrentPosition = function() {\n" +
            "    log.i(\"mraid.getCurrentPosition\");\n" +
            "    return currentPosition;\n" +
            "  };\n" +
            "\n" +
            "  mraid.getDefaultPosition = function() {\n" +
            "    log.i(\"mraid.getDefaultPosition\");\n" +
            "    return defaultPosition;\n" +
            "  };\n" +
            "\n" +
            "  mraid.getExpandProperties = function() {\n" +
            "    log.i(\"mraid.getExpandProperties\");\n" +
            "    return expandProperties;\n" +
            "  };\n" +
            "\n" +
            "  mraid.getMaxSize = function() {\n" +
            "    log.i(\"mraid.getMaxSize \" + maxSize.width + \" x \" + maxSize.height);\n" +
            "    return maxSize;\n" +
            "  };\n" +
            "\n" +
            "  mraid.getOrientationProperties = function() {\n" +
            "    log.i(\"mraid.getOrientationProperties\");\n" +
            "    return orientationProperties;\n" +
            "  };\n" +
            "\n" +
            "  mraid.getPlacementType = function() {\n" +
            "    log.i(\"mraid.getPlacementType\");\n" +
            "    return placementType;\n" +
            "  };\n" +
            "\n" +
            "  mraid.getResizeProperties = function() {\n" +
            "    log.i(\"mraid.getResizeProperties\");\n" +
            "    return resizeProperties;\n" +
            "  };\n" +
            "\n" +
            "  mraid.getScreenSize = function() {\n" +
            "    log.i(\"mraid.getScreenSize\");\n" +
            "    return screenSize;\n" +
            "  };\n" +
            "\n" +
            "  mraid.getState = function() {\n" +
            "    log.i(\"mraid.getState\");\n" +
            "    return state;\n" +
            "  };\n" +
            "\n" +
            "  mraid.getVersion = function() {\n" +
            "    log.i(\"mraid.getVersion\");\n" +
            "    return VERSION;\n" +
            "  };\n" +
            "\n" +
            "  mraid.isViewable = function() {\n" +
            "    log.i(\"mraid.isViewable\");\n" +
            "    return isViewable;\n" +
            "  };\n" +
            "\n" +
            "  mraid.open = function(url) {\n" +
            "    log.i(\"mraid.open \" + url);\n" +
            "    callNative(\"open?url=\" + encodeURIComponent(url));\n" +
            "  };\n" +
            "\n" +
            "  mraid.playVideo = function(url) {\n" +
            "    log.i(\"mraid.playVideo \" + url);\n" +
            "    callNative(\"playVideo?url=\" + encodeURIComponent(url));\n" +
            "  };\n" +
            "\n" +
            "  mraid.removeEventListener = function(event, listener) {\n" +
            "    log.i(\"mraid.removeEventListener \" + event + \" : \" + String(listener));\n" +
            "    if (!event) {\n" +
            "      mraid.fireErrorEvent(\"Event is required.\", \"removeEventListener\");\n" +
            "      return;\n" +
            "    }\n" +
            "    if (!contains(event, EVENTS)) {\n" +
            "      mraid.fireErrorEvent(\"Unknown MRAID event: \" + event, \"removeEventListener\");\n" +
            "      return;\n" +
            "    }\n" +
            "    if (listeners.hasOwnProperty(event)) {\n" +
            "      if (listener) {\n" +
            "        var listenersForEvent = listeners[event];\n" +
            "        // try to find the given listener\n" +
            "        var len = listenersForEvent.length;\n" +
            "        for (var i = 0; i < len; i++) {\n" +
            "          var registeredListener = listenersForEvent[i];\n" +
            "          var str1 = String(listener);\n" +
            "          var str2 = String(registeredListener);\n" +
            "          if (listener === registeredListener || str1 === str2) {\n" +
            "            listenersForEvent.splice(i, 1);\n" +
            "            break;\n" +
            "          }\n" +
            "        }\n" +
            "        if (i === len) {\n" +
            "          log.i(\"listener \" + str1 + \" not found for event \" + event);\n" +
            "        }\n" +
            "        if (listenersForEvent.length === 0) {\n" +
            "          delete listeners[event];\n" +
            "        }\n" +
            "      } else {\n" +
            "        // no listener to remove was provided, so remove all listeners for given event\n" +
            "        delete listeners[event];\n" +
            "      }\n" +
            "    } else {\n" +
            "      log.i(\"no listeners registered for event \" + event);\n" +
            "    }\n" +
            "  };\n" +
            "\n" +
            "  mraid.resize = function() {\n" +
            "    log.i(\"mraid.resize\");\n" +
            "    // The only time it is valid to call resize is when the ad is\n" +
            "    // a banner currently in either default or resized state.\n" +
            "    // Trigger an error if the current state is expanded.\n" +
            "    if (placementType === PLACEMENT_TYPES.INTERSTITIAL || state === STATES.LOADING || state === STATES.HIDDEN) {\n" +
            "      // do nothing\n" +
            "      return;\n" +
            "    }\n" +
            "    if (state === STATES.EXPANDED) {\n" +
            "      mraid.fireErrorEvent(\"mraid.resize called when ad is in expanded state\", \"mraid.resize\");\n" +
            "      return;\n" +
            "    }\n" +
            "    if (!isResizeReady) {\n" +
            "      mraid.fireErrorEvent(\"mraid.resize is not ready to be called\", \"mraid.resize\");\n" +
            "      return;\n" +
            "    }\n" +
            "    callNative(\"resize\");\n" +
            "  };\n" +
            "\n" +
            "  mraid.setExpandProperties = function(properties) {\n" +
            "    log.i(\"mraid.setExpandProperties\");\n" +
            "\n" +
            "    if (!validate(properties, \"setExpandProperties\")) {\n" +
            "      log.e(\"failed validation\");\n" +
            "      return;\n" +
            "    }\n" +
            "\n" +
            "    var oldUseCustomClose = expandProperties.useCustomClose;\n" +
            "\n" +
            "    // expandProperties contains 3 read-write properties: width, height, and useCustomClose;\n" +
            "    // the isModal property is read-only\n" +
            "    var rwProps = [ \"width\", \"height\", \"useCustomClose\" ];\n" +
            "    for (var i = 0; i < rwProps.length; i++) {\n" +
            "      var propname = rwProps[i];\n" +
            "      if (properties.hasOwnProperty(propname)) {\n" +
            "        expandProperties[propname] = properties[propname];\n" +
            "      }\n" +
            "    }\n" +
            "\n" +
            "    // In MRAID v2.0, all expanded ads by definition cover the entire screen,\n" +
            "    // so the only property that the native side has to know about is useCustomClose.\n" +
            "    // (That is, the width and height properties are not needed by the native code.)\n" +
            "    if (expandProperties.useCustomClose !== oldUseCustomClose) {\n" +
            "      callNative(\"useCustomClose?useCustomClose=\" + expandProperties.useCustomClose);\n" +
            "    }\n" +
            "  };\n" +
            "\n" +
            "  mraid.setOrientationProperties = function(properties) {\n" +
            "    log.i(\"mraid.setOrientationProperties\");\n" +
            "\n" +
            "    if (!validate(properties, \"setOrientationProperties\")) {\n" +
            "      log.e(\"failed validation\");\n" +
            "      return;\n" +
            "    }\n" +
            "\n" +
            "    var newOrientationProperties = {};\n" +
            "    newOrientationProperties.allowOrientationChange = orientationProperties.allowOrientationChange,\n" +
            "      newOrientationProperties.forceOrientation = orientationProperties.forceOrientation;\n" +
            "\n" +
            "    // orientationProperties contains 2 read-write properties: allowOrientationChange and forceOrientation\n" +
            "    var rwProps = [ \"allowOrientationChange\", \"forceOrientation\" ];\n" +
            "    for (var i = 0; i < rwProps.length; i++) {\n" +
            "      var propname = rwProps[i];\n" +
            "      if (properties.hasOwnProperty(propname)) {\n" +
            "        newOrientationProperties[propname] = properties[propname];\n" +
            "      }\n" +
            "    }\n" +
            "\n" +
            "    // Setting allowOrientationChange to true while setting forceOrientation to either portrait or landscape\n" +
            "    // is considered an error condition.\n" +
            "    if (newOrientationProperties.allowOrientationChange &&\n" +
            "      newOrientationProperties.forceOrientation !== mraid.ORIENTATION_PROPERTIES_FORCE_ORIENTATION.NONE) {\n" +
            "      mraid.fireErrorEvent(\"allowOrientationChange is true but forceOrientation is \" + newOrientationProperties.forceOrientation,\n" +
            "        \"setOrientationProperties\");\n" +
            "      return;\n" +
            "    }\n" +
            "\n" +
            "    orientationProperties.allowOrientationChange = newOrientationProperties.allowOrientationChange;\n" +
            "    orientationProperties.forceOrientation = newOrientationProperties.forceOrientation;\n" +
            "\n" +
            "    var params =\n" +
            "      \"allowOrientationChange=\" + orientationProperties.allowOrientationChange +\n" +
            "      \"&forceOrientation=\" + orientationProperties.forceOrientation;\n" +
            "\n" +
            "    callNative(\"setOrientationProperties?\" + params);\n" +
            "  };\n" +
            "\n" +
            "  mraid.setResizeProperties = function(properties) {\n" +
            "    log.i(\"mraid.setResizeProperties\");\n" +
            "\n" +
            "    isResizeReady = false;\n" +
            "\n" +
            "    // resizeProperties contains 6 read-write properties:\n" +
            "    // width, height, offsetX, offsetY, customClosePosition, allowOffscreen\n" +
            "\n" +
            "    // The properties object passed into this function must contain width, height, offsetX, offsetY.\n" +
            "    // The remaining two properties are optional.\n" +
            "    var requiredProps = [ \"width\", \"height\", \"offsetX\", \"offsetY\" ];\n" +
            "    for (var i = 0; i < requiredProps.length; i++) {\n" +
            "      var propname = requiredProps[i];\n" +
            "      if (!properties.hasOwnProperty(propname)) {\n" +
            "        mraid.fireErrorEvent(\n" +
            "          \"required property \" + propname + \" is missing\",\n" +
            "          \"mraid.setResizeProperties\");\n" +
            "        return;\n" +
            "      }\n" +
            "    }\n" +
            "\n" +
            "    if (!validate(properties, \"setResizeProperties\")) {\n" +
            "      mraid.fireErrorEvent(\"failed validation\", \"mraid.setResizeProperties\");\n" +
            "      return;\n" +
            "    }\n" +
            "\n" +
            "    var adjustments = { \"x\": 0, \"y\": 0 };\n" +
            "\n" +
            "    var allowOffscreen = properties.hasOwnProperty(\"allowOffscreen\") ? properties.allowOffscreen : resizeProperties.allowOffscreen;\n" +
            "    if (!allowOffscreen) {\n" +
            "      if (properties.width > maxSize.width || properties.height > maxSize.height) {\n" +
            "        mraid.fireErrorEvent(\"resize width or height is greater than the maxSize width or height\", \"mraid.setResizeProperties\");\n" +
            "        return;\n" +
            "      }\n" +
            "      adjustments = fitResizeViewOnScreen(properties);\n" +
            "    } else if (!isCloseRegionOnScreen(properties)) {\n" +
            "      mraid.fireErrorEvent(\"close event region will not appear entirely onscreen\", \"mraid.setResizeProperties\");\n" +
            "      return;\n" +
            "    }\n" +
            "\n" +
            "    var rwProps = [ \"width\", \"height\", \"offsetX\", \"offsetY\", \"customClosePosition\", \"allowOffscreen\" ];\n" +
            "    for (var i = 0; i < rwProps.length; i++) {\n" +
            "      var propname = rwProps[i];\n" +
            "      if (properties.hasOwnProperty(propname)) {\n" +
            "        resizeProperties[propname] = properties[propname];\n" +
            "      }\n" +
            "    }\n" +
            "\n" +
            "    var params =\n" +
            "      \"width=\" + resizeProperties.width +\n" +
            "      \"&height=\" + resizeProperties.height +\n" +
            "      \"&offsetX=\" + (resizeProperties.offsetX + adjustments.x) +\n" +
            "      \"&offsetY=\" + (resizeProperties.offsetY + adjustments.y) +\n" +
            "      \"&customClosePosition=\" + resizeProperties.customClosePosition +\n" +
            "      \"&allowOffscreen=\" + resizeProperties.allowOffscreen;\n" +
            "\n" +
            "    callNative(\"setResizeProperties?\" + params);\n" +
            "\n" +
            "    isResizeReady = true;\n" +
            "  };\n" +
            "\n" +
            "  mraid.storePicture = function(url) {\n" +
            "    log.i(\"mraid.storePicture \" + url);\n" +
            "    callNative(\"storePicture?url=\" + encodeURIComponent(url));\n" +
            "  };\n" +
            "\n" +
            "  mraid.supports = function(feature) {\n" +
            "    log.i(\"mraid.supports \" + feature + \" \" + supportedFeatures[feature]);\n" +
            "    var retval = supportedFeatures[feature];\n" +
            "    if (typeof retval === \"undefined\") {\n" +
            "      retval = false;\n" +
            "    }\n" +
            "    return retval;\n" +
            "  };\n" +
            "\n" +
            "  mraid.useCustomClose = function(isCustomClose) {\n" +
            "    log.i(\"mraid.useCustomClose \" + isCustomClose);\n" +
            "    if (expandProperties.useCustomClose !== isCustomClose) {\n" +
            "      expandProperties.useCustomClose = isCustomClose;\n" +
            "      callNative(\"useCustomClose?useCustomClose=\" + expandProperties.useCustomClose);\n" +
            "    }\n" +
            "  };\n" +
            "\n" +
            "  /***************************************************************************\n" +
            "   * helper methods called by SDK\n" +
            "   **************************************************************************/\n" +
            "\n" +
            "  // setters to change state\n" +
            "\n" +
            "  mraid.setCurrentPosition = function(x, y, width, height) {\n" +
            "    log.i(\"mraid.setCurrentPosition \" + x + \",\" + y + \",\" + width + \",\" + height);\n" +
            "\n" +
            "    var previousSize = {};\n" +
            "    previousSize.width = currentPosition.width;\n" +
            "    previousSize.height = currentPosition.height;\n" +
            "    log.i(\"previousSize \" + previousSize.width + \",\" + previousSize.height);\n" +
            "\n" +
            "    currentPosition.x = x;\n" +
            "    currentPosition.y = y;\n" +
            "    currentPosition.width = width;\n" +
            "    currentPosition.height = height;\n" +
            "\n" +
            "    if (width !== previousSize.width || height !== previousSize.height) {\n" +
            "      mraid.fireSizeChangeEvent(width, height);\n" +
            "    }\n" +
            "  };\n" +
            "\n" +
            "  mraid.setDefaultPosition = function(x, y, width, height) {\n" +
            "    log.i(\"mraid.setDefaultPosition \" + x + \",\" + y + \",\" + width + \",\" + height);\n" +
            "    defaultPosition.x = x;\n" +
            "    defaultPosition.y = y;\n" +
            "    defaultPosition.width = width;\n" +
            "    defaultPosition.height = height;\n" +
            "  };\n" +
            "\n" +
            "  mraid.setExpandSize = function(width, height) {\n" +
            "    log.i(\"mraid.setExpandSize \" + width + \"x\" + height);\n" +
            "    expandProperties.width = width;\n" +
            "    expandProperties.height = height;\n" +
            "  };\n" +
            "\n" +
            "  mraid.setMaxSize = function(width, height) {\n" +
            "    log.i(\"mraid.setMaxSize \" + width + \"x\" + height);\n" +
            "    maxSize.width = width;\n" +
            "    maxSize.height = height;\n" +
            "  };\n" +
            "\n" +
            "  mraid.setPlacementType = function(pt) {\n" +
            "    log.i(\"mraid.setPlacementType \" + pt);\n" +
            "    placementType = pt;\n" +
            "  };\n" +
            "\n" +
            "  mraid.setScreenSize = function(width, height) {\n" +
            "    log.i(\"mraid.setScreenSize \" + width + \"x\" + height);\n" +
            "    screenSize.width = width;\n" +
            "    screenSize.height = height;\n" +
            "  };\n" +
            "\n" +
            "  mraid.setSupports = function(feature, supported) {\n" +
            "    log.i(\"mraid.setSupports \" + feature + \" \" + supported);\n" +
            "    supportedFeatures[feature] = supported;\n" +
            "  };\n" +
            "\n" +
            "  // methods to fire events\n" +
            "\n" +
            "  mraid.fireErrorEvent = function(message, action) {\n" +
            "    log.i(\"mraid.fireErrorEvent \" + message + \" \" + action);\n" +
            "    fireEvent(mraid.EVENTS.ERROR, message, action);\n" +
            "  };\n" +
            "\n" +
            "  mraid.fireReadyEvent = function() {\n" +
            "    log.i(\"mraid.fireReadyEvent\");\n" +
            "    fireEvent(mraid.EVENTS.READY);\n" +
            "  };\n" +
            "\n" +
            "  mraid.fireSizeChangeEvent = function(width, height) {\n" +
            "    log.i(\"mraid.fireSizeChangeEvent \" + width + \"x\" + height);\n" +
            "    fireEvent(mraid.EVENTS.SIZECHANGE, width, height);\n" +
            "  };\n" +
            "\n" +
            "  mraid.fireStateChangeEvent = function(newState) {\n" +
            "    log.i(\"mraid.fireStateChangeEvent \" + newState);\n" +
            "    if (state !== newState) {\n" +
            "      state = newState;\n" +
            "      fireEvent(mraid.EVENTS.STATECHANGE, state);\n" +
            "    }\n" +
            "  };\n" +
            "\n" +
            "  mraid.fireViewableChangeEvent = function(newIsViewable) {\n" +
            "    log.i(\"mraid.fireViewableChangeEvent \" + newIsViewable);\n" +
            "    if (isViewable !== newIsViewable) {\n" +
            "      isViewable = newIsViewable;\n" +
            "      fireEvent(mraid.EVENTS.VIEWABLECHANGE, isViewable);\n" +
            "    }\n" +
            "  };\n" +
            "\n" +
            "  /***************************************************************************\n" +
            "   * internal helper methods\n" +
            "   **************************************************************************/\n" +
            "\n" +
            "  var callNative = function(command) {\n" +
            "    var iframe = document.createElement(\"IFRAME\");\n" +
            "    iframe.setAttribute(\"src\", \"mraid://\" + command);\n" +
            "    document.documentElement.appendChild(iframe);\n" +
            "    iframe.parentNode.removeChild(iframe);\n" +
            "    iframe = null;\n" +
            "  };\n" +
            "\n" +
            "  var fireEvent = function(event) {\n" +
            "    var args = Array.prototype.slice.call(arguments);\n" +
            "    args.shift();\n" +
            "    log.i(\"fireEvent \" + event + \" [\" + args.toString() + \"]\");\n" +
            "    var eventListeners = listeners[event];\n" +
            "    if (eventListeners) {\n" +
            "      var len = eventListeners.length;\n" +
            "      log.i(len + \" listener(s) found\");\n" +
            "      for (var i = 0; i < len; i++) {\n" +
            "        eventListeners[i].apply(null, args);\n" +
            "      }\n" +
            "    } else {\n" +
            "      log.i(\"no listeners found\");\n" +
            "    }\n" +
            "  };\n" +
            "\n" +
            "  var contains = function(value, array) {\n" +
            "    for (var i in array) {\n" +
            "      if (array[i] === value) {\n" +
            "        return true;\n" +
            "      }\n" +
            "    }\n" +
            "    return false;\n" +
            "  };\n" +
            "\n" +
            "  // The action parameter is a string which is the name of the setter function which called this function\n" +
            "  // (in other words, setExpandPropeties, setOrientationProperties, or setResizeProperties).\n" +
            "  // It serves both as the key to get the the appropriate set of validating functions from the allValidators object\n" +
            "  // as well as the action parameter of any error event that may be thrown.\n" +
            "  var validate = function(properties, action) {\n" +
            "    var retval = true;\n" +
            "    var validators = allValidators[action];\n" +
            "    for (var prop in properties) {\n" +
            "      var validator = validators[prop];\n" +
            "      var value = properties[prop];\n" +
            "      if (validator && !validator(value)) {\n" +
            "        mraid.fireErrorEvent(\"Value of property \" + prop + \" (\" + value + \") is invalid\", \"mraid.\" + action);\n" +
            "        retval = false;\n" +
            "      }\n" +
            "    }\n" +
            "    return retval;\n" +
            "  };\n" +
            "\n" +
            "  var allValidators = {\n" +
            "    \"setExpandProperties\": {\n" +
            "      // In MRAID 2.0, the only property in expandProperties we actually care about is useCustomClose.\n" +
            "      // Still, we'll do a basic sanity check on the width and height properties, too.\n" +
            "      \"width\" : function(width) {\n" +
            "        return !isNaN(width);\n" +
            "      },\n" +
            "      \"height\" : function(height) {\n" +
            "        return !isNaN(height);\n" +
            "      },\n" +
            "      \"useCustomClose\" : function(useCustomClose) {\n" +
            "        return (typeof useCustomClose === \"boolean\");\n" +
            "      }\n" +
            "    },\n" +
            "    \"setOrientationProperties\": {\n" +
            "      \"allowOrientationChange\" : function(allowOrientationChange) {\n" +
            "        return (typeof allowOrientationChange === \"boolean\");\n" +
            "      },\n" +
            "      \"forceOrientation\" : function(forceOrientation) {\n" +
            "        var validValues = [ \"portrait\",\"landscape\",\"none\" ];\n" +
            "        return validValues.indexOf(forceOrientation) !== -1;\n" +
            "      }\n" +
            "    },\n" +
            "    \"setResizeProperties\": {\n" +
            "      \"width\" : function(width) {\n" +
            "        return !isNaN(width) && width >= 50;\n" +
            "      },\n" +
            "      \"height\" : function(height) {\n" +
            "        return !isNaN(height) && height >= 50;\n" +
            "      },\n" +
            "      \"offsetX\" : function(offsetX) {\n" +
            "        return !isNaN(offsetX);\n" +
            "      },\n" +
            "      \"offsetY\" : function(offsetY) {\n" +
            "        return !isNaN(offsetY);\n" +
            "      },\n" +
            "      \"customClosePosition\" : function(customClosePosition) {\n" +
            "        var validPositions = [ \"top-left\",\"top-center\",\"top-right\",\"center\",\"bottom-left\",\"bottom-center\",\"bottom-right\" ];\n" +
            "        return validPositions.indexOf(customClosePosition) !== -1;\n" +
            "      },\n" +
            "      \"allowOffscreen\" : function(allowOffscreen) {\n" +
            "        return (typeof allowOffscreen === \"boolean\");\n" +
            "      }\n" +
            "    }\n" +
            "  };\n" +
            "\n" +
            "  function isCloseRegionOnScreen(properties) {\n" +
            "    log.d(\"isCloseRegionOnScreen\");\n" +
            "    log.d(\"defaultPosition \" + defaultPosition.x + \" \" + defaultPosition.y);\n" +
            "    log.d(\"offset \" + properties.offsetX + \" \" + properties.offsetY);\n" +
            "\n" +
            "    var resizeRect = {};\n" +
            "    resizeRect.x = defaultPosition.x + properties.offsetX;\n" +
            "    resizeRect.y = defaultPosition.y + properties.offsetY;\n" +
            "    resizeRect.width = properties.width;\n" +
            "    resizeRect.height = properties.height;\n" +
            "    printRect(\"resizeRect\", resizeRect);\n" +
            "\n" +
            "    var customClosePosition = properties.hasOwnProperty(\"customClosePosition\") ?\n" +
            "      properties.customClosePosition : resizeProperties.customClosePosition;\n" +
            "    log.d(\"customClosePosition \" + customClosePosition);\n" +
            "\n" +
            "    var closeRect = { \"width\": 50, \"height\": 50 };\n" +
            "\n" +
            "    if (customClosePosition.search(\"left\") !== -1) {\n" +
            "      closeRect.x = resizeRect.x;\n" +
            "    } else if (customClosePosition.search(\"center\") !== -1) {\n" +
            "      closeRect.x = resizeRect.x + (resizeRect.width / 2) - 25;\n" +
            "    } else if (customClosePosition.search(\"right\") !== -1) {\n" +
            "      closeRect.x = resizeRect.x + resizeRect.width - 50;\n" +
            "    }\n" +
            "\n" +
            "    if (customClosePosition.search(\"top\") !== -1) {\n" +
            "      closeRect.y = resizeRect.y;\n" +
            "    } else if (customClosePosition === \"center\") {\n" +
            "      closeRect.y = resizeRect.y + (resizeRect.height / 2) - 25;\n" +
            "    } else if (customClosePosition.search(\"bottom\") !== -1) {\n" +
            "      closeRect.y = resizeRect.y + resizeRect.height - 50;\n" +
            "    }\n" +
            "\n" +
            "    var maxRect = { \"x\": 0, \"y\": 0 };\n" +
            "    maxRect.width = maxSize.width;\n" +
            "    maxRect.height = maxSize.height;\n" +
            "\n" +
            "    return isRectContained(maxRect, closeRect);\n" +
            "  }\n" +
            "\n" +
            "  function fitResizeViewOnScreen(properties) {\n" +
            "    log.d(\"fitResizeViewOnScreen\");\n" +
            "    log.d(\"defaultPosition \" + defaultPosition.x + \" \" + defaultPosition.y);\n" +
            "    log.d(\"offset \" + properties.offsetX + \" \" + properties.offsetY);\n" +
            "\n" +
            "    var resizeRect = {};\n" +
            "    resizeRect.x = defaultPosition.x + properties.offsetX;\n" +
            "    resizeRect.y = defaultPosition.y + properties.offsetY;\n" +
            "    resizeRect.width = properties.width;\n" +
            "    resizeRect.height = properties.height;\n" +
            "    printRect(\"resizeRect\", resizeRect);\n" +
            "\n" +
            "    var maxRect = { \"x\": 0, \"y\": 0 };\n" +
            "    maxRect.width = maxSize.width;\n" +
            "    maxRect.height = maxSize.height;\n" +
            "\n" +
            "    var adjustments = { \"x\": 0, \"y\": 0 };\n" +
            "\n" +
            "    if (isRectContained(maxRect, resizeRect)) {\n" +
            "      log.d(\"no adjustment necessary\");\n" +
            "      return adjustments;\n" +
            "    }\n" +
            "\n" +
            "    if (resizeRect.x < maxRect.x) {\n" +
            "      adjustments.x = maxRect.x - resizeRect.x;\n" +
            "    } else if ((resizeRect.x + resizeRect.width) > (maxRect.x + maxRect.width)) {\n" +
            "      adjustments.x = (maxRect.x + maxRect.width) - (resizeRect.x + resizeRect.width);\n" +
            "    }\n" +
            "    log.d(\"adjustments.x \" + adjustments.x);\n" +
            "\n" +
            "    if (resizeRect.y < maxRect.y) {\n" +
            "      adjustments.y = maxRect.y - resizeRect.y;\n" +
            "    } else if ((resizeRect.y + resizeRect.height) > (maxRect.y + maxRect.height)) {\n" +
            "      adjustments.y = (maxRect.y + maxRect.height) - (resizeRect.y + resizeRect.height);\n" +
            "    }\n" +
            "    log.d(\"adjustments.y \" + adjustments.y);\n" +
            "\n" +
            "    resizeRect.x = defaultPosition.x + properties.offsetX + adjustments.x;\n" +
            "    resizeRect.y = defaultPosition.y + properties.offsetY + adjustments.y;\n" +
            "    printRect(\"adjusted resizeRect\", resizeRect);\n" +
            "\n" +
            "    return adjustments;\n" +
            "  }\n" +
            "\n" +
            "  function isRectContained(containingRect, containedRect) {\n" +
            "    log.d(\"isRectContained\");\n" +
            "    printRect(\"containingRect\", containingRect);\n" +
            "    printRect(\"containedRect\", containedRect);\n" +
            "    return (containedRect.x >= containingRect.x &&\n" +
            "      (containedRect.x + containedRect.width) <= (containingRect.x + containingRect.width) &&\n" +
            "      containedRect.y >= containingRect.y &&\n" +
            "      (containedRect.y + containedRect.height) <= (containingRect.y + containingRect.height));\n" +
            "  }\n" +
            "\n" +
            "  function printRect(label, rect) {\n" +
            "    log.d(label +\n" +
            "      \" [\" + rect.x + \",\" + rect.y + \"]\" +\n" +
            "      \",[\" + (rect.x + rect.width) + \",\" + (rect.y + rect.height) + \"]\" +\n" +
            "      \" (\" + rect.width + \"x\" + rect.height + \")\");\n" +
            "  }\n" +
            "\n" +
            "  mraid.dumpListeners = function() {\n" +
            "    var nEvents = Object.keys(listeners).length\n" +
            "    log.i(\"dumping listeners (\" + nEvents + \" events)\");\n" +
            "    for (var event in listeners) {\n" +
            "      var eventListeners = listeners[event];\n" +
            "      log.i(\"  \" + event + \" contains \" + eventListeners.length + \" listeners\");\n" +
            "      for (var i = 0; i < eventListeners.length; i++) {\n" +
            "        log.i(\"    \" +  eventListeners[i]);\n" +
            "      }\n" +
            "    }\n" +
            "  };\n" +
            "\n" +
            "}());\n";
}
