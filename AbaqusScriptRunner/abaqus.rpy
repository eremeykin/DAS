# -*- coding: mbcs -*-
#
# Abaqus/CAE Release 6.14-1 replay file
# Internal Version: 2014_06_05-01.11.02 134264
# Run by Pete on Sat Apr 25 12:34:22 2015
#

# from driverUtils import executeOnCaeGraphicsStartup
# executeOnCaeGraphicsStartup()
#: Executing "onCaeGraphicsStartup()" in the site directory ...
from abaqus import *
from abaqusConstants import *
session.Viewport(name='Viewport: 1', origin=(1.76563, 1.7627), width=259.9, 
    height=174.859)
session.viewports['Viewport: 1'].makeCurrent()
from driverUtils import executeOnCaeStartup
executeOnCaeStartup()
execfile('C:/Users/Pete/AppData/Local/Temp/script2742526196667808973.py', 
    __main__.__dict__)
#: A new model database has been created.
#: The model "Model-1" has been created.
session.viewports['Viewport: 1'].setValues(displayedObject=None)
#: START
#: C:\Users\Pete\AppData\Local\Temp\tmp_model5685478523552210835.obj
print 'RT script done'
#: RT script done
