from abaqus import *
from abaqusConstants import *
from caeModules import *
from driverUtils import executeOnCaeStartup
import math
import tempfile
import sys

executeOnCaeStartup()



def create_workpiece_part(length, inner, outer):
	s = mdb.models['Model-1'].ConstrainedSketch(name='__profile__', sheetSize=0.1)
	g, v, d, c = s.geometry, s.vertices, s.dimensions, s.constraints
	s.sketchOptions.setValues(decimalPlaces=3)
	s.setPrimaryObject(option=STANDALONE)
	s.CircleByCenterPerimeter(center=(0.0, 0.0), point1=(0.0, inner))
	s.CircleByCenterPerimeter(center=(0.0, 0.0), point1=(0.0, outer))
	p = mdb.models['Model-1'].Part(name='Part', dimensionality=THREE_D, 
		type=DEFORMABLE_BODY)
	p = mdb.models['Model-1'].parts['Part']
	p.BaseSolidExtrude(sketch=s, depth=length)
	s.unsetPrimaryObject()
	p = mdb.models['Model-1'].parts['Part']
	session.viewports['Viewport: 1'].setValues(displayedObject=p)
	del mdb.models['Model-1'].sketches['__profile__']
	__create_workpiece_partion(p.name)
	sys.__stdout__.write("Create workpiece model"+" Length:"+str(length)+" Inner:"+str(inner)+" Outer:"+str(outer)+"\r\n")
	return p.name



def __create_workpiece_partion(part_name):
	p = mdb.models['Model-1'].parts[part_name]
	p0 = p.DatumPointByCoordinate(coords=(0.0, 0.0, 0.0)).id
	p1 = p.DatumPointByCoordinate(coords=(0.1, 0.0, 0.0)).id
	p2 = p.DatumPointByCoordinate(coords=(-0.05, math.sqrt(3)/2/10, 0.0 )).id
	p3 = p.DatumPointByCoordinate(coords=(-0.05, -math.sqrt(3)/2/10, 0.0)).id
	p4 = p.DatumPointByCoordinate(coords=(0.0, 0.0, 0.1)).id
	p = mdb.models['Model-1'].parts[part_name]
	v1, e1, d1 = p.vertices, p.edges, p.datums
	c=p.cells
	p.PartitionCellByPlaneThreePoints(cells=c, point1=d1[p0], point2=d1[p1], point3=d1[p4])
	c=p.cells
	p.PartitionCellByPlaneThreePoints(cells=c, point1=d1[p0], point2=d1[p2], point3=d1[p4])
	c=p.cells
	p.PartitionCellByPlaneThreePoints(cells=c, point1=d1[p0], point2=d1[p3], point3=d1[p4])
	return p.name

	

def create_jaw_part(length, height, width):
	"""Creates a jaw part"""
	s = mdb.models['Model-1'].ConstrainedSketch(name='__profile__', sheetSize=0.1) # 
	g, v, d, c = s.geometry, s.vertices, s.dimensions, s.constraints #
	s.sketchOptions.setValues(decimalPlaces=3)
	s.setPrimaryObject(option=STANDALONE)
	s.rectangle(point1=(0.0, 0.0), point2=(length, height)) #
	p = mdb.models['Model-1'].Part(name='Jaw', dimensionality=THREE_D, 
	    type=DEFORMABLE_BODY) # 
	p = mdb.models['Model-1'].parts['Jaw']
	p.BaseSolidExtrude(sketch=s, depth=width) #
	s.unsetPrimaryObject()
	p = mdb.models['Model-1'].parts['Jaw']
	del mdb.models['Model-1'].sketches['__profile__'] #
	__create_jaw_partion(p.name, length, height, width)
	sys.__stdout__.write("Create jaw part "+" Length:"+str(length)+" Height:"+str(height)+" Width:"+str(width)+"\r\n")
	return p.name


	
def __create_jaw_partion(part_name, length, height, width):
	p = mdb.models['Model-1'].parts[part_name]
	p1 = p.DatumPointByCoordinate(coords=(length/2, 0.0, 0.0)).id
	p2 = p.DatumPointByCoordinate(coords=(length/2, height, width)).id
	p3 = p.DatumPointByCoordinate(coords=(length/2, 0, width)).id
	c, v1, e1, d1 = p.cells, p.vertices, p.edges, p.datums
	p.PartitionCellByPlaneThreePoints(cells=c, point1=d1[p1], point2=d1[p2], point3=d1[p3])
	return p.name



def create_section(section_name,material_name):
	mdb.models['Model-1'].HomogeneousSolidSection(name=section_name, material=material_name, thickness=None)
	return section_name

	

def create_material(material_name,young,poisson):
	"""Creates material with specified name, Young's modulus and Poisson ratio"""
	mdb.models['Model-1'].Material(name=material_name)
	mdb.models['Model-1'].materials[material_name].Elastic(table=((young, poisson), ))
	sys.__stdout__.write("Create material "+material_name+" Young modulus:"+str(young)+" Poisson ratio:"+str(poisson)+"\r\n")
	return material_name
	
	

def mesh_part(part_name, size, dev_factor, min_size_factor):
	p = mdb.models['Model-1'].parts[part_name]
	p.seedPart(size=size, deviationFactor=dev_factor, minSizeFactor=min_size_factor)
	c = p.cells
	p.setMeshControls(regions=c, technique=SWEEP) # 
	p = mdb.models['Model-1'].parts[part_name]
	p.generateMesh()
	sys.__stdout__.write("Mesh part"+"\r\n")
	


def create_assembly():
	a = mdb.models['Model-1'].rootAssembly
	a.DatumCsysByDefault(CARTESIAN)
	p = mdb.models['Model-1'].parts['Part']
	a.Instance(name='Part-1', part=p, dependent=ON)
	p = mdb.models['Model-1'].parts['Jaw']
	a.Instance(name='Jaw-1', part=p, dependent=ON)
	a.translate(instanceList=('Jaw-1', ), vector=(-jaw_width/2, 0.0, 0.0))
	a.rotate(instanceList=('Jaw-1', ), axisPoint=(0.0, 0.0, 0.0), axisDirection=(0.0, 0.0, 1), angle=-90.0)
	a.translate(instanceList=('Jaw-1', ), vector=(outer, 0.0, 0.0))
	a.RadialInstancePattern(instanceList=('Jaw-1', ), point=(0.0, 0.0, 0.0), axis=(0.0, 0.0, 1.0), number=3, totalAngle=360.0)
	sys.__stdout__.write("Create assembly"+"\r\n")


	
def assign_section(part_name, section_name):
	p = mdb.models['Model-1'].parts[part_name]
	cells = p.cells
	region = p.Set(cells=cells, name=part_name+'_material_region')
	p = mdb.models['Model-1'].parts[part_name]
	p.SectionAssignment(region=region, sectionName=section_name, offset=0.0, 
    offsetType=MIDDLE_SURFACE, offsetField='', 
    thicknessAssignment=FROM_SECTION)
	



Mdb()

length, outer, inner = 0.06, 0.030, 0.028
jaw_length, jaw_height, jaw_width= 0.015, 0.015, 0.015
jaw_young, jaw_poisson = 210e9, 0.29
jaw_mesh_size = 0.005
# 
workpiece_young, workpiece_poisson = 210e9, 0.29
workpiece_mesh_size = 0.015

if __name__ == "__main__":
	length, outer, inner = float(sys.argv[-4]), float(sys.argv[-3]), float(sys.argv[-2])
	jaw_length, jaw_height, jaw_width= float(sys.argv[-7]), float(sys.argv[-6]), float(sys.argv[-5])
	sys.stdout.write("START")
	workpiece_part=create_workpiece_part(length, inner, outer)
	jaw_part = create_jaw_part(jaw_width, jaw_height, jaw_length)
	j_material = create_material('Jaw_material', jaw_young, jaw_poisson)
	w_material = create_material('Workpiece_material', workpiece_young, workpiece_poisson)
	j_section = create_section(section_name='Jaw_section', material_name=j_material)
	w_section = create_section(section_name='Workpiece_section', material_name=w_material)
	mesh_part(part_name = jaw_part, size = jaw_mesh_size, dev_factor=0.1, min_size_factor = 0.1 )
	mesh_part(part_name = workpiece_part, size = workpiece_mesh_size, dev_factor=0.1, min_size_factor = 0.1 )
	assign_section(part_name = jaw_part, section_name = j_section)
	assign_section(part_name = workpiece_part, section_name = w_section)
	create_assembly()
	f = sys.argv[-1]
	a = mdb.models['Model-1'].rootAssembly
	session.viewports['Viewport: 1'].setValues(displayedObject=a)
	session.viewports['Viewport: 1'].assemblyDisplay.setValues(optimizationTasks=OFF, geometricRestrictions=OFF, stopConditions=OFF)
	session.writeOBJFile(fileName=f,canvasObjects= (session.viewports['Viewport: 1'], ))
	# f = open('C:\\Users\\Pete\\Desktop\\TEST', 'a')
	# f.close()
	sys.__stdout__.write("Completed!"+"\r\n")



