PGDMP                      }            vc-achievement    17.2    17.2     �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                           false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                           false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                           false            �           1262    55854    vc-achievement    DATABASE     �   CREATE DATABASE "vc-achievement" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Kazakh_Kazakhstan.utf8';
     DROP DATABASE "vc-achievement";
                     postgres    false            �            1259    55856    achievement_types    TABLE     �   CREATE TABLE public.achievement_types (
    achievement_id integer NOT NULL,
    name character varying(100) NOT NULL,
    description text,
    icon_url character varying(255),
    requirement character varying(255)
);
 %   DROP TABLE public.achievement_types;
       public         heap r       postgres    false            �            1259    55855 $   achievement_types_achievement_id_seq    SEQUENCE     �   CREATE SEQUENCE public.achievement_types_achievement_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 ;   DROP SEQUENCE public.achievement_types_achievement_id_seq;
       public               postgres    false    218            �           0    0 $   achievement_types_achievement_id_seq    SEQUENCE OWNED BY     m   ALTER SEQUENCE public.achievement_types_achievement_id_seq OWNED BY public.achievement_types.achievement_id;
          public               postgres    false    217            �            1259    55868    user_achievements    TABLE     �   CREATE TABLE public.user_achievements (
    user_id integer NOT NULL,
    achievement_id integer NOT NULL,
    achieved_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP
);
 %   DROP TABLE public.user_achievements;
       public         heap r       postgres    false            %           2604    55859     achievement_types achievement_id    DEFAULT     �   ALTER TABLE ONLY public.achievement_types ALTER COLUMN achievement_id SET DEFAULT nextval('public.achievement_types_achievement_id_seq'::regclass);
 O   ALTER TABLE public.achievement_types ALTER COLUMN achievement_id DROP DEFAULT;
       public               postgres    false    217    218    218            �          0    55856    achievement_types 
   TABLE DATA           e   COPY public.achievement_types (achievement_id, name, description, icon_url, requirement) FROM stdin;
    public               postgres    false    218   �       �          0    55868    user_achievements 
   TABLE DATA           Q   COPY public.user_achievements (user_id, achievement_id, achieved_at) FROM stdin;
    public               postgres    false    219   �       �           0    0 $   achievement_types_achievement_id_seq    SEQUENCE SET     R   SELECT pg_catalog.setval('public.achievement_types_achievement_id_seq', 1, true);
          public               postgres    false    217            (           2606    55865 ,   achievement_types achievement_types_name_key 
   CONSTRAINT     g   ALTER TABLE ONLY public.achievement_types
    ADD CONSTRAINT achievement_types_name_key UNIQUE (name);
 V   ALTER TABLE ONLY public.achievement_types DROP CONSTRAINT achievement_types_name_key;
       public                 postgres    false    218            *           2606    55863 (   achievement_types achievement_types_pkey 
   CONSTRAINT     r   ALTER TABLE ONLY public.achievement_types
    ADD CONSTRAINT achievement_types_pkey PRIMARY KEY (achievement_id);
 R   ALTER TABLE ONLY public.achievement_types DROP CONSTRAINT achievement_types_pkey;
       public                 postgres    false    218            ,           2606    55867 3   achievement_types achievement_types_requirement_key 
   CONSTRAINT     u   ALTER TABLE ONLY public.achievement_types
    ADD CONSTRAINT achievement_types_requirement_key UNIQUE (requirement);
 ]   ALTER TABLE ONLY public.achievement_types DROP CONSTRAINT achievement_types_requirement_key;
       public                 postgres    false    218            .           2606    55873 (   user_achievements user_achievements_pkey 
   CONSTRAINT     {   ALTER TABLE ONLY public.user_achievements
    ADD CONSTRAINT user_achievements_pkey PRIMARY KEY (user_id, achievement_id);
 R   ALTER TABLE ONLY public.user_achievements DROP CONSTRAINT user_achievements_pkey;
       public                 postgres    false    219    219            /           2606    55874 7   user_achievements user_achievements_achievement_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.user_achievements
    ADD CONSTRAINT user_achievements_achievement_id_fkey FOREIGN KEY (achievement_id) REFERENCES public.achievement_types(achievement_id) ON DELETE CASCADE;
 a   ALTER TABLE ONLY public.user_achievements DROP CONSTRAINT user_achievements_achievement_id_fkey;
       public               postgres    false    219    218    4650            �   A   x�3�t�,*.Qp���IM.���S�t.JM,IU��/-RH�&�e�8�"�H"y�0-��b���� =#�      �      x������ � �     