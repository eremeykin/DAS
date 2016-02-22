from abaqus import *
from abaqusConstants import *
from caeModules import *
from driverUtils import executeOnCaeStartup
import math
import tempfile
import sys
import os
import argparse
import math
import numpy as np
executeOnCaeStartup()

def cart2pol(x, y):
    rho = np.sqrt(x**2 + y**2)
    phi = np.arctan2(y, x)
    return(rho, math.degrees(phi))

def pol2cart(rho, phi):
    phi = math.radians(phi)
    x = rho * np.cos(phi)
    y = rho * np.sin(phi)
    return(x, y)   

def create_workpiece_part(length, inner, outer, angle, parameter):   
    s = mdb.models['Model-1'].ConstrainedSketch(name='__profile__', sheetSize=0.1)
    g, v, d, c = s.geometry, s.vertices, s.dimensions, s.constraints
    s.sketchOptions.setValues(decimalPlaces=3)
    s.setPrimaryObject(option=STANDALONE)
    s.CircleByCenterPerimeter(center=(0.0, 0.0), point1=(0.0, inner))
    s.CircleByCenterPerimeter(center=(0.0, 0.0), point1=(0.0, outer))
    p = mdb.models['Model-1'].Part(name='Part', dimensionality=THREE_D, type=DEFORMABLE_BODY)
    p = mdb.models['Model-1'].parts['Part']
    p.BaseSolidExtrude(sketch=s, depth=length)
    s.unsetPrimaryObject()
    p = mdb.models['Model-1'].parts['Part']
    session.viewports['Viewport: 1'].setValues(displayedObject=p)
    del mdb.models['Model-1'].sketches['__profile__']
    # p.PartitionCellByPlaneThreePoints(point1=(0,0,0), point2=(0,0,1), point3=(0,1,1), cells= mdb.models['Model-1'].parts['Part'].cells)
    # __create_workpiece_partion_by_angle(p.name, angle, parameter)
    sys.__stdout__.write("Create workpiece model"+" Length:"+str(length)+" Inner:"+str(inner)+" Outer:"+str(outer)+"\n")
    return p.name
    
def __create_workpiece_partion_by_angle(part_name, ang, param):
    p = mdb.models['Model-1'].parts[part_name]
    p1 = p.DatumPointByCoordinate(coords=(0.0, 0.0, 0.0))
    p2 = p.DatumPointByCoordinate(coords=(0.1, 0.0, 0.0))
    p3 = p.DatumPointByCoordinate(coords=(0.0, 0.0, 0.1))
    d = p.datums
    oxz_plane = p.DatumPlaneByThreePoints(point1=d[p1.id], point2=d[p2.id], point3=d[p3.id])
    oz_axis = p.DatumAxisByTwoPoint(point1=d[p1.id], point2=d[p3.id])
    force_plane = p.DatumPlaneByRotation(plane=d[oxz_plane.id], axis=d[oz_axis.id], angle=ang)
    p.PartitionFaceByDatumPlane(datumPlane=d[force_plane.id], faces=p.faces)
    # pickedEdges = p.edges.getSequenceFromMask(mask=('[#1 ]', ), )
    # p.PartitionEdgeByParam(edges=pickedEdges, parameter=param)
        
def create_jaw_part(length, height, width):
    """Creates a jaw part"""
    s = mdb.models['Model-1'].ConstrainedSketch(name='__profile__', sheetSize=0.1) # 
    g, v, d, c = s.geometry, s.vertices, s.dimensions, s.constraints #
    s.sketchOptions.setValues(decimalPlaces=3)
    s.setPrimaryObject(option=STANDALONE)
    s.rectangle(point1=(0.0, 0.0), point2=(length, height)) #
    p = mdb.models['Model-1'].Part(name='Jaw', dimensionality=THREE_D,  type=DEFORMABLE_BODY) # 
    p = mdb.models['Model-1'].parts['Jaw']
    p.BaseSolidExtrude(sketch=s, depth=width) #
    s.unsetPrimaryObject()
    p = mdb.models['Model-1'].parts['Jaw']
    del mdb.models['Model-1'].sketches['__profile__'] #
    __create_jaw_partion(p.name, length, height, width)
    sys.__stdout__.write("Create jaw part "+" Length:"+str(length)+" Height:"+str(height)+" Width:"+str(width)+"\n")
    return p.name
    
def __create_jaw_partion(part_name, length, height, width):
    p = mdb.models['Model-1'].parts[part_name]
    p1 = p.DatumPointByCoordinate(coords=(length/2, 0.0, 0.0)).id
    p2 = p.DatumPointByCoordinate(coords=(length/2, height, width)).id
    p3 = p.DatumPointByCoordinate(coords=(length/2, 0, width)).id
    c, v1, e1, d1 = p.cells, p.vertices, p.edges, p.datums
    p.PartitionCellByPlaneThreePoints(cells=c, point1=d1[p1], point2=d1[p2], point3=d1[p3])
        
    p1 = p.DatumPointByCoordinate(coords=(0.0, 0.0, width/2)).id
    p2 = p.DatumPointByCoordinate(coords=(length, 0.0, width/2)).id
    p3 = p.DatumPointByCoordinate(coords=(length, height, width/2)).id
    c, v1, e1, d1 = p.cells, p.vertices, p.edges, p.datums
    p.PartitionCellByPlaneThreePoints(cells=c, point1=d1[p1], point2=d1[p2], point3=d1[p3])
    

def create_section(section_name,material_name, type, thickness=None):
    if type == 'solid':
        mdb.models['Model-1'].HomogeneousSolidSection(name=section_name, material=material_name, thickness=None)
        return section_name
    if type == 'shell':
        mdb.models['Model-1'].HomogeneousShellSection(name=section_name, 
        preIntegrate=OFF, material='Workpiece_material', thicknessType=UNIFORM, 
        thickness=thickness, thicknessField='', idealization=NO_IDEALIZATION, 
        poissonDefinition=DEFAULT, thicknessModulus=None, temperature=GRADIENT, 
        useDensity=OFF, integrationRule=SIMPSON, numIntPts=5)
        return section_name
    raise Exception('Wrong section type')
    
def create_material(material_name,young,poisson):
    """Creates material with specified name, Young's modulus and Poisson ratio"""
    mdb.models['Model-1'].Material(name=material_name)
    mdb.models['Model-1'].materials[material_name].Elastic(table=((young, poisson), ))
    sys.__stdout__.write("Create material "+material_name+" Young modulus:"+str(young)+" Poisson ratio:"+str(poisson)+"\n")
    return material_name

def mesh_part(part_name, size, dev_factor, min_size_factor):
    p = mdb.models['Model-1'].parts[part_name]
    p.seedPart(size=size, deviationFactor=dev_factor, minSizeFactor=min_size_factor)

    if p.cells: # if solid 
        c = p.cells
        p.setMeshControls(regions=c, technique=SWEEP) # 
        p = mdb.models['Model-1'].parts[part_name]
    else: # if shell
        elemType1 = mesh.ElemType(elemCode=S4R, elemLibrary=STANDARD, 
        secondOrderAccuracy=OFF, hourglassControl=DEFAULT)
        elemType2 = mesh.ElemType(elemCode=S3, elemLibrary=STANDARD)    
        pickedRegions =(p.faces, )
        p.setElementType(regions=pickedRegions, elemTypes=(elemType1, elemType2))
    p.generateMesh()
    sys.__stdout__.write("Mesh part"+"\n")

def create_property(friction_coeff):
    mdb.models['Model-1'].ContactProperty('IntProp-1')
    mdb.models['Model-1'].interactionProperties['IntProp-1'].TangentialBehavior(
        formulation=PENALTY, directionality=ISOTROPIC, slipRateDependency=OFF, 
        pressureDependency=OFF, temperatureDependency=OFF, dependencies=0, table=((
        friction_coeff, ), ), shearStressLimit=None, maximumElasticSlip=FRACTION, 
        fraction=0.005, elasticSlipStiffness=None)
    mdb.models['Model-1'].interactionProperties['IntProp-1'].NormalBehavior(
        pressureOverclosure=HARD, allowSeparation=OFF, contactStiffness=DEFAULT, 
        contactStiffnessScaleFactor=1.0, clearanceAtZeroContactPressure=0.0, 
        stiffnessBehavior=LINEAR, constraintEnforcementMethod=PENALTY)

def create_assembly(jforce, outer, length, Fx, Fy, Fz, angle):
    create_property(f_coeff)# pass friction coeff
    a = mdb.models['Model-1'].rootAssembly
    a.DatumCsysByDefault(CARTESIAN)
    p = mdb.models['Model-1'].parts['Part']
    a.Instance(name='Part-1', part=p, dependent=ON)
    p = mdb.models['Model-1'].parts['Jaw']
    a.Instance(name='Jaw-1', part=p, dependent=ON)
    a.translate(instanceList=('Jaw-1', ), vector=(-jaw_width/2, 0.0, 0.0))
    a.rotate(instanceList=('Jaw-1', ), axisPoint=(0.0, 0.0, 0.0), axisDirection=(0.0, 0.0, 1), angle=-90.0)
    mid_d = float(inner-outer)/2+inner
    a.translate(instanceList=('Jaw-1', ), vector=(outer, 0.0, 0.0))
    a.RadialInstancePattern(instanceList=('Jaw-1', ), point=(0.0, 0.0, 0.0), axis=(0.0, 0.0, 1.0), number=jcount, totalAngle=360.0)
    mdb.models['Model-1'].rootAssembly.features.changeKey(fromName='Jaw-1', toName='Jaw-1-rad-1')
    sys.__stdout__.write("Create assembly"+"\n") 
    create_interaction()
    c_systems = create_CSYS()
    create_step()
    create_jaw_BSs(c_systems)
    apply_jaw_force(jforce, c_systems)
    create_tie_constraint()
    create_force(Fx, Fy, Fz, angle)
    
def create_force(Fx, Fy, Fz, angle):
    if Fx==Fy==Fz==0:
        return
    a = mdb.models['Model-1'].rootAssembly
    d = a.datums
    force_sys = a.DatumCsysByTwoLines(CYLINDRICAL, line1=d[1].axis1, line2=d[1].axis2, name='Cutting_force_csys')
    v = a.instances['Part-1'].vertices
    verts = v.getSequenceFromMask(mask=('[#1 ]', ), )
    region = a.Set(vertices=verts, name='Set-7')
    datum = mdb.models['Model-1'].rootAssembly.datums[force_sys.id]
    dct = dict((k,v) for (k,v) in [('cf1',Fx), ('cf2',Fy), ('cf3',Fz)] if v !=0)
    mdb.models['Model-1'].StaticStep(name='Step-2', previous='Step-1')
    mdb.models['Model-1'].ConcentratedForce(name='Cutting_force', createStepName='Step-2', 
        region=region, distributionType=UNIFORM, field='', localCsys=datum, **dct)
    for j in JAWS:
        jaw = 'Jaw-1-rad-'+str(j)
        mdb.models['Model-1'].loads[jaw+'load'].deactivate('Step-2')
    pass

def create_tie_constraint():
    z = jaw_length/100
    mid_d = float(inner-outer)/2+inner
    for j in JAWS:
        angle = (j-1)*(360.0/len(JAWS))
        x1, y1, x2, y2 = __get_xy(outer, angle)
        jaw = 'Jaw-1-rad-'+str(j)
        
        # select areas on Jaw
        a = mdb.models['Model-1'].rootAssembly
        s1 = a.instances[jaw].faces
        side1Faces1 = s1.findAt(((x1, y1, z), ), ((x2, y2, z), ),((x1, y1, z+jaw_length/2), ),((x2, y2, z+jaw_length/2), ))
        region1=a.Surface(side1Faces=side1Faces1, name=jaw + '_tie_surf')
        
        # select areas on Part
        a = mdb.models['Model-1'].rootAssembly
        s1 = a.instances['Part-1'].faces
        side1Faces1 = s1.findAt(((x1, y1, z), ), ((x2, y2, z), ),((x1, y1, z+jaw_length/2), ),((x2, y2, z+jaw_length/2), ))
        region2=a.Surface(side1Faces=side1Faces1, name='Part_tie_surf' + str(j))
        
        mdb.models['Model-1'].Tie(name=jaw+'_tie_constraint', master=region2, slave=region1, 
        positionToleranceMethod=COMPUTED, adjust=ON, tieRotations=ON, thickness=OFF)
    
def __get_xy(radius, ref_angle):
    x1 = radius * math.cos(math.radians(ref_angle+0.05))
    x2 = radius * math.cos(math.radians(ref_angle-0.05))
    y1 = radius * math.sin(math.radians(ref_angle+0.05))
    y2 = radius * math.sin(math.radians(ref_angle-0.05))
    return x1, y1, x2,y2

def create_interaction():
    z = jaw_length/100
    # mid_d = float(inner-outer)/2+inner
    for j in JAWS:
        jaw = 'Jaw-1-rad-'+str(j)
        angle = (j-1)*(360.0/len(JAWS))
        x1, y1, x2, y2 = __get_xy(outer, angle)
        
        # select areas on Jaw
        a = mdb.models['Model-1'].rootAssembly
        s1 = a.instances[jaw].faces
        side1Faces1 = s1.findAt(((x1, y1, z), ), ((x2, y2, z), ),((x1, y1, z+jaw_length/2), ),((x2, y2, z+jaw_length/2), ))
        region1=a.Surface(side1Faces=side1Faces1, name=jaw + '_master_surf')
        
        # select areas on Part
        a = mdb.models['Model-1'].rootAssembly
        s1 = a.instances['Part-1'].faces
        side1Faces1 = s1.findAt(((x1, y1, z), ), ((x2, y2, z), ),((x1, y1, z+jaw_length/2), ),((x2, y2, z+jaw_length/2), ))
        region2=a.Surface(side1Faces=side1Faces1, name='Part_slave_surf' + str(j))
        
        # apply interaction to Jaw and Part
        mdb.models['Model-1'].SurfaceToSurfaceContactStd(name='Int-'+str(j), 
            createStepName='Initial', master=region1, slave=region2, sliding=FINITE, 
            thickness=ON, interactionProperty='IntProp-1', adjustMethod=NONE, 
            initialClearance=OMIT, datumAxis=None, clearanceRegion=None)

def create_CSYS():
    res=[]
    for j in JAWS:
        jaw = 'Jaw-1-rad-'+str(j)
        csys_n = j+1
        a = mdb.models['Model-1'].rootAssembly
        d1 = a.instances[jaw].datums
        v1 = a.instances[jaw].vertices
        res.append(a.DatumCsysByThreePoints(origin=d1[2], point1=(0,0,0), point2=v1[7], name = jaw + '_csys-'+str(csys_n), coordSysType=CARTESIAN))
    return res

def create_jaw_BSs(c_systems):
    for j in JAWS:
        jaw = 'Jaw-1-rad-'+str(j)
        #starts from 0 cause c_systems is passed to function
        # JAWS=1,2,3; csys_n = 0,1,2
        csys_n = j - 1
        a = mdb.models['Model-1'].rootAssembly
        f1 = a.instances['Jaw-1-rad-'+str(j)].faces
        #faces1 = f1.getSequenceFromMask(mask=('[#402 ]', ), )
        x1, y1 = pol2cart(outer+jaw_width/2, 360/len(JAWS)*csys_n+0.5)
        x2, y2 = pol2cart(outer+jaw_width/2, 360/len(JAWS)*csys_n-0.5)
        
        # faces1 = f1.findAt((x1,y1, 0),(x2,y2, 0))
        # sys.__stdout__.write( str(faces1))
        faces1 = f1.getSequenceFromMask(mask=('[#402 ]', ), )
        region = a.Set(faces = faces1, name='Jaw_BS_set-'+str(j))
        datum = mdb.models['Model-1'].rootAssembly.datums[c_systems[csys_n].id]
        mdb.models['Model-1'].DisplacementBC(name='BC-'+str(j), createStepName='Initial', 
            region=region, u1=UNSET, u2=SET, u3=SET, ur1=UNSET, ur2=UNSET, ur3=UNSET, 
            amplitude=UNSET, distributionType=UNIFORM, fieldName='', localCsys=datum)
  
def create_step():
    mdb.models['Model-1'].StaticStep(name='Step-1', previous='Initial')

def apply_jaw_force(value, c_systems):
    print c_systems
    for j in JAWS:
        jaw = 'Jaw-1-rad-'+str(j)
        # JAWS=1,2,3; csys_n = 0,1,2
        csys_n = j - 1
        a = mdb.models['Model-1'].rootAssembly
        v1 = a.instances[jaw].vertices
        verts1 = v1.getSequenceFromMask(mask=('[#2 ]', ), )
        region = a.Set(vertices=verts1, name=jaw+'force_set')
        datum = mdb.models['Model-1'].rootAssembly.datums[c_systems[csys_n].id]
        mdb.models['Model-1'].ConcentratedForce(name=jaw + 'load', createStepName='Step-1', 
            region=region, cf1=value, distributionType=UNIFORM, field='', localCsys=datum)

def assign_section(part_name, section_name):
    p = mdb.models['Model-1'].parts[part_name]
    if p.cells:  # if solid
        region = p.Set(cells=p.cells, name=part_name+'_material_region')
    else:        # if shell
        region = p.Set(faces=p.faces, name=part_name+'_material_region')
    p.SectionAssignment(region=region, sectionName=section_name, offset=0.0, 
                offsetType=TOP_SURFACE, offsetField='', 
                thicknessAssignment=FROM_SECTION)          

def run_job(home):
    sys.__stdout__.write("Run job"+"\n")
    Job1 = mdb.Job(name='Job-1', model='Model-1', description='', type=ANALYSIS, 
    atTime=None, waitMinutes=0, waitHours=0, queue=None, memory=90, 
    memoryUnits=PERCENTAGE, getMemoryFromAnalysis=True, 
    explicitPrecision=SINGLE, nodalOutputPrecision=SINGLE, echoPrint=OFF, 
    modelPrint=OFF, contactPrint=OFF, historyPrint=OFF, userSubroutine='', 
    scratch=home, resultsFormat=ODB, multiprocessingMode=DEFAULT, numCpus=1, 
    numGPUs=0)
    sys.__stdout__.write("Submit"+"\n")
    Job1.submit(consistencyChecking=OFF)
    Job1.waitForCompletion()
    sys.__stdout__.write("Job completed"+"\n") 
    
def path_by_z(z=0.06, delta=0.0001):
    ns = session.odbs['C:/DASworkspace/Job-1.odb'].rootAssembly.nodeSets[' ALL NODES'].nodes
    for i in range(len(ns)):
        if ns[i][0].instanceName == 'PART-1':
            break
    part_ns = ns[i]
    nodes = [x for x in part_ns if z+delta>=x.coordinates[2]>=z-delta]
    pts = tuple((p.coordinates[0],p.coordinates[1],p.coordinates[2]) for p in nodes if outer + delta >= cart2pol(p.coordinates[0],p.coordinates[1])[0] >= outer-delta )
    pts = sorted(pts, key=lambda t: cart2pol(t[0],t[1])[1])
    session.Path(name='Path-1', type=POINT_LIST, expression=pts) 
       
    
def create_path():
    return session.Path(name='Path-1', type=EDGE_LIST, expression=(('PART-1', ((84, 3, 2, 
    1), (96, 3, 2, 1), (108, 3, 2, 1), (120, 3, 2, 1), (132, 3, 2, 1), (144, 3, 
    2, 1), (156, 3, 2, 1), (168, 3, 2, 1), (180, 3, 2, 1), (192, 3, 2, 1), (
    204, 3, 2, 1), (216, 3, 2, 1), (228, 3, 2, 1), (240, 3, 2, 1), (252, 3, 2, 
    1), (264, 3, 2, 1), (276, 3, 2, 1), (288, 3, 2, 1), (300, 3, 2, 1), (312, 
    3, 2, 1), (324, 3, 2, 1), (336, 3, 2, 1), (348, 3, 2, 1), (360, 3, 2, 1), (
    372, 3, 2, 1), (384, 3, 2, 1), (396, 3, 2, 1), (408, 3, 2, 1), (420, 3, 2, 
    1), (432, 3, 2, 1), (12, 3, 2, 1), (24, 3, 2, 1), (36, 3, 2, 1), (48, 3, 2, 
    1), (60, 3, 2, 1), )), ))

Mdb()

length, outer, inner = 0.06, 0.030, 0.028
anglef = 20
fparameter = 0.9
jcount = 3
JAWS = range(1,jcount+1)
jaw_length, jaw_height, jaw_width= 0.015, 0.015, 0.015
jaw_young, jaw_poisson = 210e15, 0.29
jaw_mesh_size = 0.0015
jforce = 300
refresh = False
f_coeff = 0.3

workpiece_young, workpiece_poisson = 210e9, 0.29
workpiece_mesh_size = 0.003

tanf,radf,axlf = 0, 0, 0

rplane = 0
tplane = 1

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description='argparse')
    
    parser.add_argument("-dir", nargs=1)
    parser.add_argument("-refresh", nargs=1)
    parser.add_argument("-inner", nargs=1)
    parser.add_argument("-outer", nargs=1)
    parser.add_argument("-length", nargs=1)
    parser.add_argument("-anglef", nargs=1)
    parser.add_argument("-workpiece_young", nargs=1)
    parser.add_argument("-workpiece_poisson", nargs=1)
    parser.add_argument("-jaw_length", nargs=1)
    parser.add_argument("-jaw_height", nargs=1)
    parser.add_argument("-jaw_width", nargs=1)
    parser.add_argument("-jaw_young", nargs=1)
    parser.add_argument("-jaw_poisson", nargs=1)
    parser.add_argument("-tanf", nargs=1)
    parser.add_argument("-radf", nargs=1)
    parser.add_argument("-axlf", nargs=1)
    parser.add_argument("-jcount", nargs=1)
    parser.add_argument("-jforce", nargs=1)
    parser.add_argument("-rplane", nargs=1)
    parser.add_argument("-tplane", nargs=1)

    args, unknown = parser.parse_known_args()
    sys.__stdout__.write(str(args))
    sys.__stdout__.flush()
    res = vars(args)
    try:
        home = res['dir'][0]
    except:
        res = {"dir" : ("C:\\DASworkspace",), 
                "refresh" : (False,),
                "inner" : (inner,),
                "outer" : (outer,),
                "length" :(length,),
                "anglef" : (anglef,),
                "workpiece_young" : (workpiece_young,),
                "workpiece_poisson" : (workpiece_poisson,),
                "jforce" : (jforce,),
                "jaw_length" : (jaw_length,),
                "jaw_height" : (jaw_height,),
                "jaw_width" : (jaw_width,),
                "jaw_young" : (jaw_young,),
                "jaw_poisson": (jaw_poisson,),
                "tanf":(tanf,),
                "radf":(radf,),
                "axlf":(axlf,),
                "jcount" : (jcount,),
                "rplane":(rplane,),
                "tplane":(tplane,)
        }
    for r in res:
        try:
            res[r][0] = res[r][0].replace(",",".")
        except:
            pass
    home = res['dir'][0]
    refresh = res['refresh'][0] in ["True","true"]
    inner = float(res['inner'][0])
    outer = float(res['outer'][0])
    length = float(res['length'][0])
    anglef = float(res['anglef'][0])
    workpiece_young = float(res['workpiece_young'][0])
    workpiece_poisson = float(res['workpiece_poisson'][0])
    jforce = float(res['jforce'][0])
    jaw_length= float(res['jaw_length'][0])
    jaw_height= float(res['jaw_height'][0])
    jaw_width= float(res['jaw_width'][0])
    jaw_young = float(res['jaw_young'][0])
    jaw_poisson = float(res['jaw_poisson'][0])
    tanf = float(res['tanf'][0])
    radf = float(res['radf'][0])
    axlf = float(res['axlf'][0])
    jcount = int(float(res['jcount'][0]))
    JAWS = range(1,jcount+1)
    
    rplane = float(res['rplane'][0])
    tplane = float(res['tplane'][0])
    
    os.chdir(home)
    sys.__stdout__.write("Working directory="+str(home)+"\n")
    sys.__stdout__.flush()

    sys.__stdout__.write("Refresh="+str(refresh)+"\n")
    sys.__stdout__.flush()

    sys.__stdout__.write("START\n")
    sys.__stdout__.flush()

    workpiece_part=create_workpiece_part(length, inner, outer, anglef, fparameter)
    jaw_part = create_jaw_part(jaw_width, jaw_height, jaw_length)
    j_material = create_material('Jaw_material', jaw_young, jaw_poisson)
    w_material = create_material('Workpiece_material', workpiece_young, workpiece_poisson)
    j_section = create_section(section_name='Jaw_section', material_name=j_material,type='solid')
    w_section = create_section(section_name='Workpiece_section', material_name=w_material,type='solid')#,thickness=0.002)
    
    mesh_part(part_name = jaw_part, size = jaw_mesh_size, dev_factor=0.1, min_size_factor = 0.1 )
    mesh_part(part_name = workpiece_part, size = workpiece_mesh_size, dev_factor=0.1, min_size_factor = 0.1 )

    assign_section(part_name = workpiece_part, section_name = w_section)
    assign_section(part_name = jaw_part, section_name = j_section)
    
    create_assembly(jforce, outer, length, tanf, radf, axlf, anglef)
    a = mdb.models['Model-1'].rootAssembly
    session.viewports['Viewport: 1'].setValues(displayedObject=a)
    session.viewports['Viewport: 1'].assemblyDisplay.setValues(optimizationTasks=OFF, geometricRestrictions=OFF, stopConditions=OFF)
    session.viewports['Viewport: 1'].odbDisplay.basicOptions.setValues(renderShellThickness=ON)
    session.writeOBJFile(fileName=home+"/tmp_model.obj",canvasObjects= (session.viewports['Viewport: 1'], ))
    if refresh:
        sys.__stdout__.write("Refreshed!"+"\n")
        sys.__stdout__.flush()
        sys.exit()
    run_job(home)
        
    o1 = session.openOdb(name=home+'/Job-1.odb')
    odb = session.odbs[home+'/Job-1.odb']
    scratchOdb = session.ScratchOdb(odb)
    scratchOdb.rootAssembly.DatumCsysByThreePoints(name='CSYS-1', coordSysType=CYLINDRICAL, origin=(0.0, 0.0, 0.0), point1=(1.0, 0.0, 0.0), point2=(0.0, 1.0, 0.0))
    dtm = session.scratchOdbs[home+'/Job-1.odb'].rootAssembly.datumCsyses['CSYS-1']
    
    session.viewports['Viewport: 1'].setValues(displayedObject=o1)
    session.viewports['Viewport: 1'].odbDisplay.display.setValues(plotState=(CONTOURS_ON_DEF, ))
    session.viewports['Viewport: 1'].odbDisplay.setPrimaryVariable(variableLabel='U', outputPosition=NODAL, refinement=(COMPONENT, 'U1'), )
    session.viewports['Viewport: 1'].odbDisplay.basicOptions.setValues(transformationType=USER_SPECIFIED, datumCsys=dtm)
    session.xyReportOptions.setValues(numberFormat=SCIENTIFIC)
    # to file 

    # obj file
    session.writeOBJFile(fileName=home+"/tmp_model.obj",canvasObjects= (session.viewports['Viewport: 1'], ))
    
    session.printOptions.setValues(vpDecorations=OFF, compass=ON)
    session.pngOptions.setValues(imageSize=(2000, 1500))
    leaf = dgo.LeafFromPartInstance(partInstanceName=('PART-1', ))
    session.viewports['Viewport: 1'].odbDisplay.displayGroup.replace(leaf=leaf)
    
    # U plot
    session.viewports['Viewport: 1'].view.setValues(session.views['Iso'])
    session.printToFile(fileName= home+'/uplot', format=PNG, canvasObjects=(session.viewports['Viewport: 1'], ))
    
    # S plot    
    session.viewports['Viewport: 1'].odbDisplay.setPrimaryVariable(variableLabel='S', outputPosition=INTEGRATION_POINT, refinement=(INVARIANT, 'Mises'), )    
    session.printToFile(fileName= home+'/splot', format=PNG, canvasObjects=(session.viewports['Viewport: 1'], ))
    
    #path data
    
    
    
    # exit()
    # pth = create_path()
    # session.XYDataFromPath(name='XYData-1', path=pth, includeIntersections=False, 
                    # projectOntoMesh=False, pathStyle=PATH_POINTS, numIntervals=10, 
                    # projectionTolerance=0, shape=DEFORMED, labelType=TRUE_DISTANCE)
    # sys.__stdout__.write("XY data created"+"\n")
    # sys.__stdout__.flush()
    # x0 = session.xyDataObjects['XYData-1']
    # session.writeXYReport(fileName=home+'/abaqus.rpt', appendMode=OFF, xyData=(x0, ))
    # session.writeOBJFile(fileName=home+"/tmp_model.obj",canvasObjects= (session.viewports['Viewport: 1'], ))
    # sys.__stdout__.write("Script completed!"+"\n")
    # sys.__stdout__.flush()





