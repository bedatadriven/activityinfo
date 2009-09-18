Attribute VB_Name = "Module1"
Option Explicit

Private docWidth As Double
Private docHeight As Double

Private pMapEnvelope As IEnvelope
Private mapHeight As Double
Private mapWidth As Double
Private pNS As IXMLNamespaces

Private UUPerPU As Double ' User Units Per Page Unit
Private UUPerPt As Double ' User Units per Point
Private UUPerPx As Double ' User Units per Pixel

Const Precision As Long = 0
Const PrettyPrint As Boolean = True

Const SvgNs As String = "http://www.w3.org/2000/svg"
Const XlinkNs As String = "http://www.w3.org/1999/xlink"
Const MapNs As String = "http://www.activityinfo.org/schemas/svg-map.xsd"

Public Sub ExportDoc()
    
    Dim pMap As IMap
    Dim pMxDoc As IMxDocument
    Dim pGraphicContainer As IGraphicsContainer
    Dim pElement As IElement
    Dim pProj As IProjectedCoordinateSystem
    
    Set pMxDoc = ThisDocument
    pMxDoc.PageLayout.Page.QuerySize docWidth, docHeight
    
    
    ' Pfad des Kartendokuments feststellen
    Dim pTemplates As ITemplates
    Dim lTempCount As Long
    Dim strDocPath As String

    Set pTemplates = Application.Templates
    lTempCount = pTemplates.Count
    strDocPath = Left(pTemplates.Item(lTempCount - 1), (Len(pTemplates.Item(lTempCount - 1)) - Len(Application.Document.Title)))

    ' Create and open the exportfile
    Dim fileName As String
    Dim baseName As String
    
    baseName = Left(Application.Document.Title, Len(Application.Document.Title) - 4)
    fileName = strDocPath & baseName & ".svg"
        
    ' AB: use esri's xml writer to properly deal with character encoding
    
    Dim pWriter As IXMLWriter
    Set pWriter = New XMLWriter
    
    Dim pXMLStream As IXMLStream
    Set pXMLStream = New XMLStream
    
    '*** This sets output to XML Stream ***
    pWriter.WriteTo pXMLStream
    
    'XML Document-Head
    pWriter.WriteXMLDeclaration
    pWriter.WriteNewLine
    
    ' XML DTD statement
    pWriter.WriteXML "<!DOCTYPE svg PUBLIC ""-//W3C//DTD SVG 1.1//EN"" ""http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd"">"
    pWriter.WriteNewLine
    
    ' reference external stylesheet
'    pWriter.WriteXML "<?xml-stylesheet href=""" & baseName & ".css"" type=""text/css""?>"
'    pWriter.WriteNewLine
'
    ' define namespaces
    ' we want to embed additional info into the svg document, eg. fields,
    ' georeferencing, etc. so we need to define our own namespace.
    ' Since we encode fields as attributes, I think each document would need its own
    ' schema.
    
    
    Dim pNS As IXMLNamespaces
    Set pNS = New XMLNamespaces
    pNS.AddNamespace "", SvgNs
    pNS.AddNamespace "xlink", XlinkNs
    pNS.AddNamespace "m", MapNs
    
    ' Choose the user units per Point. Increasing this value will
    ' increase the resolution of the vector graphics
    UUPerPt = 1
    
    Dim PageUnits As String
    Select Case pMxDoc.PageLayout.Page.Units
    Case esriUnits.esriInches
        PageUnits = "in"
        UUPerPU = 72 * UUPerPt
    Case esriUnits.esriCentimeters
        PageUnits = "cm"
        UUPerPU = 28 * UUPerPt   ' pts per cm i think
    Case esriUnits.esriMillimeters
        PageUnits = "mm"
        UUPerPU = 2.8 * UUPerPt
    Case esriUnits.esriPoints
        PageUnits = "pt"
        UUPerPU = 1 * UUPerPt
    End Select
      
    UUPerPx = UUPerPU / 72
          
     'SVG Definition
    pWriter.WriteStartTag "svg", SvgNs, Attributes( _
        "width", docWidth & PageUnits, _
        "height", docHeight & PageUnits, _
        "viewbox", "0 0 " & Str(docWidth * UUPerPU) & " " & Str(docHeight * UUPerPU)), _
        pNS, False
    If PrettyPrint Then pWriter.WriteNewLine
    
    Dim Elements As New Collection
    
    Set pGraphicContainer = pMxDoc.PageLayout
    pGraphicContainer.Reset
    Set pElement = pGraphicContainer.Next
    
    While Not pElement Is Nothing
        Elements.Add pElement
        Set pElement = pGraphicContainer.Next
    Wend
    
    Dim i As Long
    For i = Elements.Count To 1 Step -1
        Set pElement = Elements(i)
        If TypeOf pElement Is IMapFrame Then
            WriteMap pWriter, pElement
        ElseIf TypeOf pElement Is ITextElement Then
            writeTextElement pWriter, pElement, pElement.Geometry, False
        End If
    Next i
        
    
    pWriter.WriteEndTag
      
    '*** Save stream to the file ***
    pXMLStream.SaveToFile fileName
    
    'done
    MsgBox "File as " & fileName & " saved."

End Sub

Private Function writeFrame(pWriter As IXMLWriter, pFrameProps As IFrameProperties)



End Function

Private Function writeFrameBackgrond(pWriter As IXMLWriter, pElement As IElement)

    Dim pFrame As IFrameElement
    Set pFrame = pElement
    
    Dim pSB As ISymbolBackground
    Set pSB = pFrame.Background
    
    If Not pSB.FillSymbol.Color.NullColor Then
        
        pWriter.WriteStartTag "rect", SvgNs, Attributes( _
            "x", (pElement.Geometry.Envelope.XMin * UUPerPU) + (pSB.Gap * UUPerPt), _
            "y", ((docHeight - pElement.Geometry.Envelope.YMax) * UUPerPU) + (pSB.Gap * UUPerPt), _
            "width", (pElement.Geometry.Envelope.Width * UUPerPU) + (pSB.Gap * 2 * UUPerPt), _
            "height", (pElement.Geometry.Envelope.Height * UUPerPU) + (pSB.Gap * 2 * UUPerPt), _
            "fill", colorHex(pSB.FillSymbol.Color), _
            "stroke", "none"), Nothing, True
    End If
End Function

Private Function WriteMap(pWriter As IXMLWriter, pElement As IElement)

     'Map-Frame Size
     ' AB: in my locale, this comes out inches, i.e. mapWidth = 6
     ' which is not great for a viewbox because it means lots of decimal points
     ' in the output.
     ' Let's scale this to points.
     
     Dim pFrame As IMapFrame
     Set pFrame = pElement
     
     Dim pMap As IMap
     Set pMap = pFrame.Map
     
     
     ' Eek. Can figure out where the units for this geometry is; no spatial reference set
     
     Dim mapX As Long
     Dim mapY As Long
     
     Let mapX = pElement.Geometry.Envelope.XMin * UUPerPU
     Let mapY = (docHeight - pElement.Geometry.Envelope.YMax) * UUPerPU
    
     mapWidth = pElement.Geometry.Envelope.Width * UUPerPU
     mapHeight = pElement.Geometry.Envelope.Height * UUPerPU
   
    ' Calculate a simple linear transform that scripts can
    ' use to plot XY coordinates
    ' in most (of my) cases this will be sufficient
        
    Dim pGeoCS As ISpatialReference
    Dim pSrEnv As New SpatialReferenceEnvironment
    Set pGeoCS = pSrEnv.CreateGeographicCoordinateSystem(esriSRGeoCS_WGS1984)
    
    Set pMapEnvelope = pFrame.MapBounds.Envelope
    
    Dim pGeoMapBounds As IGeometry
    Set pGeoMapBounds = pFrame.MapBounds
    pGeoMapBounds.Project pGeoCS
    
    Dim sx As Double
    Dim sy As Double
    Dim tx As Double
    Dim ty As Double
    
    sx = mapWidth / pGeoMapBounds.Envelope.Width
    tx = -(pGeoMapBounds.Envelope.XMin * sx)
    sy = -mapHeight / pGeoMapBounds.Envelope.Height
    ty = (pGeoMapBounds.Envelope.YMax * sy)
    
    Dim mapId As String
    Let mapId = "map_" & Replace(pMap.Name, " ", "_")
    
    'SVG Definition
    pWriter.WriteStartTag "svg", SvgNs, Attributes( _
        "id", mapId, _
        "x", mapX, _
        "y", mapY, _
        "width", mapWidth, _
        "height", mapHeight, _
        "viewbox", "0 0 " & Str(mapWidth) & " " & Str(mapHeight), _
        "m:sx", Str(sx), _
        "m:sy", Str(sy), _
        "m:tx", Str(tx), _
        "m:ty", Str(ty)), pNS, False
    If PrettyPrint Then pWriter.WriteNewLine
    
    If Not pFrame.Background Is Nothing Then
    
        Dim pSB As ISymbolBackground
        Set pSB = pFrame.Background
        
        If Not pSB.FillSymbol.Color.NullColor Then
            
            pWriter.WriteStartTag "rect", SvgNs, Attributes( _
                "x", -(pSB.Gap * UUPerPt), _
                "y", -(pSB.Gap * UUPerPt), _
                "width", (pElement.Geometry.Envelope.Width * UUPerPU) + (pSB.Gap * 2 * UUPerPt), _
                "height", (pElement.Geometry.Envelope.Height * UUPerPU) + (pSB.Gap * 2 * UUPerPt), _
                "fill", colorHex(pSB.FillSymbol.Color), _
                "stroke", "none"), Nothing, True
        End If
    End If
    
        
    ' Loop thru layers, creating styles for feature layers
    Dim i As Integer
    pWriter.WriteStartTag "style", SvgNs, Nothing, Nothing, False
    If PrettyPrint Then pWriter.WriteNewLine
    
    For i = 0 To pMap.LayerCount - 1
        If pMap.Layer(i).Visible And TypeOf pMap.Layer(i) Is IFeatureLayer Then
            Dim pFLayer As IFeatureLayer
            Set pFLayer = pMap.Layer(i)
            
            If pFLayer.FeatureClass.ShapeType <> esriGeometryPoint Then
                writeLayerStyle pWriter, mapId, pMap.Layer(i)
            End If
        End If
    Next i
    pWriter.WriteEndTag
    If PrettyPrint Then pWriter.WriteNewLine
    
    ' Loop thru layers, creating defs for point symbols
    
    pWriter.WriteStartTag "defs", SvgNs, Nothing, Nothing, False
    If PrettyPrint Then pWriter.WriteNewLine
    
    For i = 0 To pMap.LayerCount - 1
        If pMap.Layer(i).Visible And TypeOf pMap.Layer(i) Is IFeatureLayer Then
            Set pFLayer = pFrame.Map.Layer(i)
            If Not pFLayer.FeatureClass Is Nothing Then
                If pFLayer.FeatureClass.ShapeType = esriGeometryPoint Then
                    
                    writePointDef pWriter, pMap, pFLayer
                End If
            End If
        End If
    Next i
    pWriter.WriteEndTag
    If PrettyPrint Then pWriter.WriteNewLine
    
    'Loop thru all Layers and write out features
    

    Dim svgIDField As Long

    For i = (pMap.LayerCount - 1) To 0 Step -1
        If pMap.Layer(i).Visible And TypeOf pMap.Layer(i) Is FeatureLayer Then
            If TypeOf pMap.Layer(i) Is IFeatureLayer Then
                WriteLayer pMap, pFrame.MapBounds, pWriter, pMap.Layer(i)
            End If
        End If
        If PrettyPrint Then pWriter.WriteNewLine
    Next i
    
    ' Write out the frame
    
    If Not pFrame.Border Is Nothing Then
        Dim pSBdr As ISymbolBorder
        Set pSBdr = pFrame.Border
         
        If Not pSBdr.LineSymbol.Color.NullColor Then
        
            pWriter.WriteStartTag "rect", SvgNs, Attributes( _
                "x", -(pSBdr.Gap * UUPerPt), _
                "y", -(pSBdr.Gap * UUPerPt), _
                "width", (pElement.Geometry.Envelope.Width * UUPerPU) + (pSBdr.Gap * 2 * UUPerPt), _
                "height", (pElement.Geometry.Envelope.Height * UUPerPU) + (pSBdr.Gap * 2 * UUPerPt), _
                "fill", "none", _
                "stroke", colorHex(pSBdr.LineSymbol.Color), _
                "stroke-width", pSBdr.LineSymbol.Width * UUPerPx), Nothing, True
        
    End If
    
    writeAnnotations pWriter, pMap
            
    
    pWriter.WriteEndTag
    

    End If

End Function

Private Function writeAnnotations(pWriter As IXMLWriter, pMap As IMap)

    ' Write Map Annotations
    
    Dim pCLayer As ICompositeLayer
    Set pCLayer = pMap.BasicGraphicsLayer
    
    Dim k As Long
    For k = 0 To pCLayer.Count - 1
        
        Dim pGc As IGraphicsContainer
        Set pGc = pCLayer.Layer(k)
        
        Dim pGL As IGraphicsLayer
        Set pGL = pCLayer.Layer(k)
       
        pWriter.WriteStartTag "g", SvgNs, Attributes( _
            "class", "labels "), Nothing, False
            
        If PrettyPrint Then pWriter.WriteNewLine
                    
        pGc.Reset
        Dim pelem As IElement
        Set pelem = pGc.Next
                
        While Not pelem Is Nothing
            If TypeOf pelem Is ITextElement Then
                Dim pTextElem As ITextElement
                Set pTextElem = pelem
                Debug.Print pTextElem.Text
                
                Dim pTextGeom As IGeometry
                Set pTextGeom = Clone(pelem.Geometry)
                pTextGeom.Project pMap.SpatialReference
                
                writeTextElement pWriter, pTextElem, pTextGeom, True
                
            End If
            Set pelem = pGc.Next
        Wend
        
        pWriter.WriteEndTag
        If PrettyPrint Then pWriter.WriteNewLine
        
    Next k

End Function

Private Function writeTextElement(pWriter As IXMLWriter, pTextElem As ITextElement, pTextGeom As IGeometry, isMapAnnotation As Boolean)
    Dim pTextEnv As IEnvelope
    Set pTextEnv = pTextGeom.Envelope

    Dim pt As WKSPoint
    Dim textAnchor As String
    If pTextElem.symbol.HorizontalAlignment = esriTHACenter Or pTextElem.symbol.HorizontalAlignment = esriTHAFull Then
        pt.X = (pTextEnv.XMin + pTextEnv.XMax) / 2
        textAnchor = "middle"
    ElseIf pTextElem.symbol.HorizontalAlignment = esriTHALeft Then
        pt.X = pTextEnv.XMin
        textAnchor = "start"
    ElseIf pTextElem.symbol.HorizontalAlignment = esriTHARight Then
        pt.X = pTextEnv.XMax
        textAnchor = "end"
    End If
    
    
    Debug.Print pTextEnv.Height
      
    Dim pFmtTxt As IFormattedTextSymbol
    Set pFmtTxt = pTextElem.symbol
    
    Dim fontSize As Double
    Let fontSize = pFmtTxt.Font.size * UUPerPt
    
    If isMapAnnotation Then
        pt.Y = pTextEnv.YMin
        pt = WksPt2Page(pt)
    Else
        pt.X = pt.X * UUPerPU
        pt.Y = (docHeight - (pTextEnv.YMax)) * UUPerPU + fontSize
    End If
    
    Dim strText As String
    Let strText = pTextElem.Text
    If pFmtTxt.Case = esriTCAllCaps Or pFmtTxt.Case = esriTCSmallCaps Then
        strText = UCase(strText)
    End If
    
    Dim pAttribs As IXMLAttributes
    Set pAttribs = Attributes( _
        "x", CStr(pt.X), _
        "y", CStr(pt.Y), _
        "fill", colorHex(pFmtTxt.Color), _
        "text-anchor", textAnchor, _
        "font-family", pFmtTxt.Font.Name, _
        "font-size", fontSize, _
        "alignment-baseline", "middle")
        
    If pFmtTxt.Font.Italic Then
        pAttribs.AddAttribute "font-style", "", "italic"
    ElseIf pFmtTxt.Font.Bold Then
        pAttribs.AddAttribute "font-weight", "", "bold"
    End If
    
    
    pWriter.WriteStartTag "text", SvgNs, pAttribs, _
        Nothing, False

    Dim lines As Variant
    Let lines = Split(strText, vbCrLf)
    If UBound(lines) = LBound(lines) Then
        pWriter.WriteText strText
    Else
        If PrettyPrint Then pWriter.WriteNewLine
        Dim j As Long
        For j = LBound(lines) To UBound(lines)
            Dim offset As Double
            If j = LBound(lines) Then
                offset = -((UBound(lines) - LBound(lines) + 1))
            Else
                offset = 1
            End If
            
            pWriter.WriteStartTag "tspan", SvgNs, Attributes( _
                "x", CStr(pt.X), _
                "dy", offset & "em"), Nothing, False
            pWriter.WriteText lines(j)
            pWriter.WriteEndTag
            If PrettyPrint Then pWriter.WriteNewLine
        Next
    End If
    pWriter.WriteEndTag
    If PrettyPrint Then pWriter.WriteNewLine

End Function

Private Function colorHex(Color As IColor) As String
    If Color.NullColor Then
        colorHex = "none"
    Else
        colorHex = Right(Hex(Color.RGB), 6)
        While (Len(colorHex) < 6)
            colorHex = "0" & colorHex
        Wend
        
        'reverse
        colorHex = "#" & Right(colorHex, 2) & Mid(colorHex, 3, 2) & Left(colorHex, 2)
    End If
End Function


Private Function featureSymbolName(pMap As IMap, pLayer As ILayer, pFeature As IFeature) As String

    Dim pGFL As IGeoFeatureLayer
    Set pGFL = pLayer
    
    featureSymbolName = pMap.Name & "_" & pLayer.Name
    
    If TypeOf pGFL.Renderer Is IUniqueValueRenderer Then
        Dim pUVR As IUniqueValueRenderer
        Set pUVR = pGFL.Renderer
        
        Dim value As String
        Let value = pFeature.value(pFeature.Fields.FindField(pUVR.Field(0)))
        
        featureSymbolName = featureSymbolName & "_" & value
    End If
    
    featureSymbolName = Replace(featureSymbolName, " ", "_")
End Function

Private Function featureClassName(pLayer As IFeatureLayer, pFeature As IFeature) As String


    Dim pGFL As IGeoFeatureLayer
    Set pGFL = pLayer
    
    Dim lclass As String
    Let lclass = layerClassName(pLayer)
    
    If TypeOf pGFL.Renderer Is IUniqueValueRenderer Then
        Dim pUVR As IUniqueValueRenderer
        Set pUVR = pGFL.Renderer
        
        Dim value As String
        Let value = pFeature.value(pFeature.Fields.FindField(pUVR.Field(0)))
        
        featureClassName = Replace(value, " ", "_")
        
    End If
    
End Function

Private Function writeMarker(pWriter As IXMLWriter, pSymbol As IMarkerSymbol)

    If TypeOf pSymbol Is ISimpleMarkerSymbol Then
        Dim pSMS As ISimpleMarkerSymbol
        Set pSMS = pSymbol
        
        Dim pAttribs As IXMLAttributes
        Set pAttribs = New XMLAttributes
        pAttribs.AddAttribute "fill", "", colorHex(pSMS.Color)
        pAttribs.AddAttribute "stroke", "", IIf(pSMS.Outline, colorHex(pSMS.OutlineColor), "none")
        If pSMS.Outline Then
            pAttribs.AddAttribute "stroke-width", "", pSMS.OutlineSize * UUPerPx
        End If
        
        Dim size As Long
        Let size = Int(pSMS.size * UUPerPx)
        
        If pSMS.Style = esriSMSCircle Then
            
            pAttribs.AddAttribute "cx", "", (pSMS.XOffset * UUPerPx)
            pAttribs.AddAttribute "cy", "", (pSMS.YOffset * UUPerPx)
            pAttribs.AddAttribute "r", "", size / 2
            
            pWriter.WriteStartTag "circle", SvgNs, pAttribs, Nothing, True
        ElseIf pSMS.Style = esriSMSSquare Then
            pAttribs.AddAttribute "x", "", ((pSMS.XOffset - (pSMS.size / 2)) * UUPerPx)
            pAttribs.AddAttribute "y", "", ((pSMS.YOffset - (pSMS.size / 2)) * UUPerPx)
            pAttribs.AddAttribute "width", "", size
            pAttribs.AddAttribute "height", "", size
            
            pWriter.WriteStartTag "rect", SvgNs, pAttribs, Nothing, True
        End If
        pWriter.WriteNewLine
    End If

End Function

Private Function writePointSymbol(pWriter As IXMLWriter, id, pSymbol As ISymbol)

    pWriter.WriteStartTag "g", SvgNs, Attributes("id", id), Nothing, False
    pWriter.WriteNewLine
    
    If TypeOf pSymbol Is IMultiLayerMarkerSymbol Then
        Dim pMLMS As IMultiLayerMarkerSymbol
        Set pMLMS = pSymbol
        
        Dim i As Long
        For i = pMLMS.LayerCount - 1 To 0 Step -1
            writeMarker pWriter, pMLMS.Layer(i)
            pWriter.WriteNewLine
        Next i
    ElseIf TypeOf pSymbol Is ISimpleMarkerSymbol Then
        writeMarker pWriter, pMLMS.Layer(i)
        pWriter.WriteNewLine
    End If
    
    pWriter.WriteEndTag
    pWriter.WriteNewLine
End Function

Private Function writePointDef(pWriter As IXMLWriter, pMap As IMap, pLayer As ILayer)

    Dim pGFL As IGeoFeatureLayer
    Set pGFL = pLayer

    If TypeOf pGFL.Renderer Is ISimpleRenderer Then
    
        Dim pSR As ISimpleRenderer
        Set pSR = pGFL.Renderer
        
        writePointSymbol pWriter, pMap.Name & "_" & Replace(pLayer.Name, " ", "_"), pSR.symbol
        
    ElseIf TypeOf pGFL.Renderer Is IUniqueValueRenderer Then
    
        Dim pUVR As IUniqueValueRenderer
        Set pUVR = pGFL.Renderer
        
        Dim i As Long
        For i = 0 To pUVR.ValueCount - 1
            writePointSymbol pWriter, pMap.Name & "_" & Replace(pLayer.Name, " ", "_") & "_" & pUVR.value(i), pUVR.symbol(pUVR.value(i))
        Next
    
    End If
    
    If PrettyPrint Then pWriter.WriteNewLine

End Function

Private Function writeLayerStyle(pWriter As IXMLWriter, mapId As String, pLayer As IFeatureLayer)


    Dim pGFL As IGeoFeatureLayer
    Set pGFL = pLayer
    
    Dim lclass As String
    Let lclass = layerClassName(pLayer)
    
    Dim pathSelector As String
    Let pathSelector = "#" & mapId & " g." & lclass & " path"
    
    If TypeOf pGFL.Renderer Is ISimpleRenderer Then
        Dim pSimple As ISimpleRenderer
        Set pSimple = pGFL.Renderer
        
        writeSymbolRule pWriter, pathSelector, pSimple.symbol
       
    ElseIf TypeOf pGFL.Renderer Is IUniqueValueRenderer Then
        
        Dim pUVR As IUniqueValueRenderer
        Set pUVR = pGFL.Renderer
        
        ' write the default rule for this layer
        writeSymbolRule pWriter, pathSelector, pUVR.DefaultSymbol

        Dim i As Long
        For i = 0 To pUVR.ValueCount - 1
                                 
            Dim value As String
            Let value = pUVR.value(i)
            
            writeSymbolRule pWriter, pathSelector & "." & Replace(value, " ", "_"), pUVR.symbol(value)
            
        Next i
    End If
End Function

Private Function writeSymbolRule(pWriter As IXMLWriter, selector As String, symbol As ISymbol)
    
   If TypeOf symbol Is IFillSymbol Then
        Dim pFS As IFillSymbol
        Set pFS = symbol
    
        pWriter.WriteText selector & " { fill: " & colorHex(pFS.Color) & "; "
        pWriter.WriteText " stroke: " & colorHex(pFS.Outline.Color) & "; stroke-width: " & pFS.Outline.Width * UUPerPx & "; }"
        pWriter.WriteNewLine
    ElseIf TypeOf symbol Is ILineSymbol Then
    
        Dim pLS As ILineSymbol
        Set pLS = symbol
        
        pWriter.WriteText selector & " { fill: none; stroke: " & colorHex(pLS.Color) & "; stroke-width: " & pLS.Width * UUPerPx & "; }"
        pWriter.WriteNewLine
    ElseIf TypeOf symbol Is ICartographicLineSymbol Then
        
        Dim pCLS As ICartographicLineSymbol
        Set pCLS = symbol
        
        pWriter.WriteText selector & " { fill: none; stroke: " & colorHex(pCLS.Color) & "; stroke-width: " & pCLS.Width * UUPerPx & "; }"
        pWriter.WriteNewLine
    
    End If
End Function

Private Function layerClassName(pLayer As IFeatureLayer) As String
    layerClassName = Replace(pLayer.Name, " ", "_")
End Function

Private Function WriteLayer(pMap As IMap, pMapBounds As IEnvelope, pWriter As IXMLWriter, pLayer As ILayer)

    On Error GoTo LayerError

    Dim pFLayer As IFeatureLayer
    Set pFLayer = pLayer

    ' create the definition query object
    Dim pQuery As IQueryFilter
    Dim pFilter As IFeatureLayerDefinition
    Set pFilter = pFLayer
    If Len(pFilter.DefinitionExpression) > 0 Then
        Set pQuery = New QueryFilter
        pQuery.WhereClause = pFilter.DefinitionExpression
    End If
    
    'set Cursor
    Dim pFCur As IFeatureCursor
    Set pFCur = pFLayer.FeatureClass.Search(pQuery, True)
    Dim pFeature As IFeature
    Set pFeature = pFCur.NextFeature
    
    'Layer as SVG Group
    
    Dim pLayerAttribs As IXMLAttributes
    Set pLayerAttribs = Attributes("class", "layer " & layerClassName(pFLayer))
    
    
    ' check for special instructions
    Dim pLGP As ILayerGeneralProperties
    Set pLGP = pLayer
    
    Dim adminCodeField As String
    
    If InStr(1, pLGP.LayerDescription, "=") >= 1 Then
        Dim kv() As String
        kv = Split(pLGP.LayerDescription, "=")
        If IsNumeric(kv(0)) Then
            pLayerAttribs.AddAttribute "adminLevelId", MapNs, kv(0)
            adminCodeField = kv(1)
        End If
    End If

    Dim pMapEnv As IEnvelope
    Set pMapEnv = Clone(pMapBounds)
    pMapEnv.Project pMap.SpatialReference


    pWriter.WriteStartTag "g", SvgNs, pLayerAttribs, Nothing, False
    
    If PrettyPrint Then pWriter.WriteNewLine
               
            
    Do While Not pFeature Is Nothing
        
        Dim pMetaAttribs As Collection
        Set pMetaAttribs = New Collection
        
        If adminCodeField <> "" Then
            pMetaAttribs.Add "m:code"
            pMetaAttribs.Add Value2String(pFeature.value(pFeature.Fields.FindField(adminCodeField)))
        End If
        
            
        
        ' Write metaattributes
        
'                For j = 1 To fieldsToShowInfo.Count
'                    Set pFieldInfo = fieldsToShowInfo(j)
'                    pMetaAttribs.Add "m:" & pFieldInfo.Alias
'                    pMetaAttribs.Add pFeature.Value(fieldsToShowIndexes(j))
'                    Debug.Print pFieldInfo.Alias & " = " & Value2String(pFeature.Value(fieldsToShowIndexes(j)))
'                Next j
'

        ' project geometry
        
        
        
        Dim pGeom As IGeometry
        Set pGeom = pFeature.ShapeCopy
        pGeom.Project pMap.SpatialReference
        
        Dim pGeomTR As ITopologicalOperator
        Set pGeomTR = pGeom
        pGeomTR.Clip pMapEnv

        If Not pGeom.IsEmpty Then
            WriteShape pWriter, pMap, pFLayer, pFeature, pGeom, pMetaAttribs
        End If
        
        Set pFeature = pFCur.NextFeature

    Loop
    
    pWriter.WriteEndTag
ExitHere:
    Exit Function
LayerError:
    Debug.Print Err.Description

End Function

Private Function Value2String(v As Variant) As String
    On Error Resume Next
    Value2String = CStr(v)
End Function

' Short cut function for ESRI's unwieldly obj model

Public Function Attributes(ParamArray parray() As Variant) As IXMLAttributes
    Set Attributes = New XMLAttributes

    Dim i As Long
    Let i = LBound(parray)
    While i <= UBound(parray)
        If VarType(parray(i)) = vbObject Then
            Dim obj As Object
            Set obj = parray(i)
            If TypeOf obj Is Collection Then
                Dim col As Collection
                Set col = obj
                Dim j As Long
                For j = 1 To col.Count Step 2
                    Attributes.AddAttribute col(j), "", col(j + 1)
                Next
            End If
            i = i + 1
        Else
            Attributes.AddAttribute parray(i), "", parray(i + 1)
            i = i + 2
        End If
    Wend
End Function

Public Function WriteShape(pWriter As IXMLWriter, pMap As IMap, pLayer As ILayer, pFeature As IFeature, pGeom As IGeometry, pMetaAttribs As Collection) As String

    ' This function summarizes much of the information held in a Shape. ]
    ' The pGeom parameter should hold the required Geometry object, and the lFormatNum
    ' parameter indicates the required number of decimal places for the coordinates.

    Dim pPC As IPointCollection
    Dim pPt As IPoint
    Dim strSub As String
    Dim lCount As Long, i As Long
    
    ' Report to user if Shape is nothing or has no geometry set (is empty).
    If pGeom Is Nothing Then
        Err.Raise "1", "Shape is Nothing"
    ElseIf pGeom.IsEmpty Then
        Exit Function
        Err.Raise "2", "Geometry is empty"
    End If
    
    ' Identify the type of geometry.
    Select Case pGeom.GeometryType
        Case esriGeometryPoint
            ' AB: For a Point, reference the symbol defined in the header
            ' this can later styled as necessary
            Dim pt As WKSPoint
            pt = Pt2Page(pGeom)
            pWriter.WriteStartTag "use", SvgNs, Attributes(pMetaAttribs, _
                "x", CStr(pt.X), _
                "y", CStr(pt.Y), _
                "xlink:href", "#" & featureSymbolName(pMap, pLayer, pFeature)), Nothing, True
            If PrettyPrint Then pWriter.WriteNewLine
        Case esriGeometryMultipoint
            ' For a Multipoint, report coordinates of each point.
            ' This part was never tested, but should work so far...
            Dim pGc As IGeometryCollection
            Set pGc = pGeom
            lCount = pGc.GeometryCount - 1
            
            Dim symbolRef As String
            Let symbolRef = "#" & featureSymbolName(pMap, pLayer, pFeature)

            'loop thru Features
            'AB: wrap the element in <g> to maintain the logical model
            pWriter.WriteStartTag "g", SvgNs, Attributes(pMetaAttribs), Nothing, False
            If PrettyPrint Then pWriter.WriteNewLine
            For i = 0 To lCount
                Set pPC = pGc.Geometry(i)
                pt = Pt2Page(pPC)
                pWriter.WriteStartTag "use", SvgNs, Attributes(pMetaAttribs, _
                    "x", Str(pt.X), _
                    "y", Str(pt.Y), _
                    "xlink:href", symbolRef), Nothing, True
                If PrettyPrint Then pWriter.WriteNewLine
            Next i
        Case esriGeometryPolyline, esriGeometryPolygon
            ' For a Polyline or Polygon, report the number of vertices
            'and coordinates of each vertex in each part.
            Set pGc = pGeom
            lCount = pGc.GeometryCount - 1
 
            'loop thru Features
            Dim strShape As String
            For i = 0 To lCount
                strShape = strShape & PointCollAsString(pGc.Geometry(i))
            Next i
            ' only write the shape if it isn't too small ot have colappsed

            If (Len(strShape) > 0) Then
                
                Dim pAttribs As IXMLAttributes
                Set pAttribs = Attributes(pMetaAttribs)
            
                Dim className As String
                Let className = featureClassName(pLayer, pFeature)
                                            
                If Len(className) <> 0 Then
                    pAttribs.AddAttribute "class", "", className
                End If
                
                pAttribs.AddAttribute "d", "", strShape
    
                                         
                pWriter.WriteStartTag "path", SvgNs, pAttribs, Nothing, True
                If PrettyPrint Then pWriter.WriteNewLine
            End If
        Case Else
            strShape = "Shape type ist not supported"
    End Select
    
End Function

Private Function PointCollAsString(pPC As IPointCollection) As String

    ' This function creates a string of infomation for a PointCollection.

    Dim lCount As Long, i As Long
    Dim pPt As IPoint
    Dim strCollection As String, strPoint As String

    Dim pt As WKSPoint
    Dim lastPt As WKSPoint
    Dim pointsWritten As Long
    Dim lastCmd As String
    
    ' Iterate the point collection.
    lCount = pPC.PointCount - 1
    'strCollection = CStr(pPC.PointCount) & vbCrLf
    For i = 0 To lCount
        pt = Pt2Page(pPC.Point(i))

        ' Create a string of information for each Point in the collection.
        If pointsWritten = 0 Then
            strCollection = strCollection & "M" & CStr(pt.X) & " " & CStr(pt.Y)
            lastPt = pt
            pointsWritten = pointsWritten + 1
            lastCmd = "M"
        Else
            Dim dist As Double
            dist = Sqr((pt.X - lastPt.X) ^ 2 + (pt.Y - lastPt.Y) ^ 2)
            If dist > 2 Then
                
                ' coords can either be expressed as relative or absolute
                ' we'll encode this coord both ways and see which one is shorter.
                
                Dim lrel As String
                Dim labs As String
                
                Dim relCmd As String
                If lastCmd = "l" Then
                    If pt.X < lastPt.X Then
                        relCmd = "" ' the negative sign can function as delimter
                    Else
                        relCmd = "l" ' we need some delimter
                    End If
                Else
                    relCmd = "l"
                End If
                        
                lrel = relCmd & CStr(pt.X - lastPt.X) & IIf(pt.Y < lastPt.Y, "", " ") & CStr(pt.Y - lastPt.Y)
                labs = "L" & CStr(pt.X) & " " & CStr(pt.Y)
                
                If Len(lrel) < Len(labs) Then
                    strCollection = strCollection & lrel
                    lastCmd = "l"
                Else
                    strCollection = strCollection & labs
                    lastCmd = "L"
                End If
                lastPt = pt
                pointsWritten = pointsWritten + 1
            End If
        End If
    Next i
    
    If (pointsWritten > 1) Then
        PointCollAsString = strCollection
    End If
End Function

Private Function Pt2Page(pPt As IPoint) As WKSPoint
    Pt2Page.X = Round((((pPt.X - pMapEnvelope.XMin) * mapWidth) / pMapEnvelope.Width), Precision)
    Pt2Page.Y = Round((mapHeight - (((pPt.Y - pMapEnvelope.YMin) * mapHeight) / pMapEnvelope.Height)), Precision)
End Function


Private Function WksPt2Page(pPt As WKSPoint) As WKSPoint
    WksPt2Page.X = Round((((pPt.X - pMapEnvelope.XMin) * mapWidth) / pMapEnvelope.Width), Precision)
    WksPt2Page.Y = Round((mapHeight - (((pPt.Y - pMapEnvelope.YMin) * mapHeight) / pMapEnvelope.Height)), Precision)
End Function

Private Function Clone(clonable As IClone)
    Set Clone = clonable.Clone
End Function









